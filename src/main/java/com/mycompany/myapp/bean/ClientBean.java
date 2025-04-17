package com.mycompany.myapp.bean;

import com.mycompany.myapp.dao.ClientDAO;
import com.mycompany.myapp.model.Client;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;
import org.primefaces.event.SelectEvent;

@Named
@SessionScoped
public class ClientBean implements Serializable {

    @Inject 
    private ClientDAO clientDAO;

    private Client client = new Client();
    private Client selectedClient;
    private Long selectedClientId;
    private List<Client> listClients;
    private boolean modeEdition = false;
    private boolean clientSelectionne = false;
    
    // Nouveaux champs pour la recherche
    private String searchQuery = "";
    private List<Client> filteredClients;

    @PostConstruct
    public void init() {
        loadClients();
    }

    private void loadClients() {
        this.listClients = clientDAO.findAll();
        this.filteredClients = this.listClients; // Par défaut, on affiche tous les clients
    }
    
    // Nouvelle méthode de recherche
    public void searchClients() {
        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            // Si la recherche est vide, afficher tous les clients
            this.filteredClients = this.listClients;
            return;
        }
        
        this.filteredClients = clientDAO.searchByNomOrPrenom(searchQuery.trim());
        
        if (this.filteredClients.isEmpty()) {
            addMessage("Information", "Aucun client trouvé pour: \"" + searchQuery + "\"");
        } else {
            addMessage("Résultats", this.filteredClients.size() + " client(s) trouvé(s)");
        }
    }
    
    // Reset la recherche
    public void resetSearch() {
        this.searchQuery = "";
        this.filteredClients = this.listClients;
    }

    public void ajouterClient() {
        try {
            if (client.getId() == null) {
                clientDAO.create(client);
                addMessage("Succès", "Client ajouté avec succès");
            } else {
                clientDAO.update(client);
                addMessage("Succès", "Client modifié avec succès");
            }
            // Recharger la liste des clients
            loadClients();
            // Réinitialiser le client et le client sélectionné
            this.client = new Client();
            this.selectedClientId = null;
            this.selectedClient = null;
            this.clientSelectionne = false;
            this.modeEdition = false;
        } catch (Exception e) {
            addMessage("Erreur", "Une erreur s'est produite: " + e.getMessage());
        }
    }

    public void onRowSelect(SelectEvent<Client> event) {
        // Récupérer le client sélectionné
        Client selectedClient = event.getObject();
        
        // Créer une nouvelle instance de Client pour éviter les problèmes de référence
        this.client = new Client();

        // Copier toutes les valeurs
        this.client.setId(selectedClient.getId());
        this.client.setNom(selectedClient.getNom());
        this.client.setPrenom(selectedClient.getPrenom());
        this.client.setDateNaissance(selectedClient.getDateNaissance());
        this.client.setCategorie(selectedClient.getCategorie());
        this.client.setEmail(selectedClient.getEmail());
        this.client.setTelephone(selectedClient.getTelephone());
        this.client.setNewsletter(selectedClient.getNewsletter());
        this.client.setRayon(selectedClient.getRayon());

        // Stocker l'ID du client sélectionné
        this.selectedClientId = selectedClient.getId();
        this.clientSelectionne = true;
        this.modeEdition = false;
        
        addMessage("Information", "Client sélectionné: " + selectedClient.getNom() + " " + selectedClient.getPrenom());
    }

    public void activerModeEdition() {
        this.modeEdition = true;
        addMessage("Information", "Mode édition activé");
    }

    public void preparerModification(Client c) {
        // Créer une nouvelle instance de Client pour éviter les problèmes de référence
        this.client = new Client();

        // Copier toutes les valeurs
        this.client.setId(c.getId());
        this.client.setNom(c.getNom());
        this.client.setPrenom(c.getPrenom());
        this.client.setDateNaissance(c.getDateNaissance());
        this.client.setCategorie(c.getCategorie());
        this.client.setEmail(c.getEmail());
        this.client.setTelephone(c.getTelephone());
        this.client.setNewsletter(c.getNewsletter());
        this.client.setRayon(c.getRayon());

        // Stocker l'ID du client sélectionné
        this.selectedClientId = c.getId();
        this.clientSelectionne = true;
        this.modeEdition = true;
    }

    public void supprimerClient(Client c) {
        try {
            clientDAO.delete(c);
            loadClients();
            addMessage("Suppression", "Client supprimé avec succès");

            // Si on supprime le client en cours d'édition, réinitialiser
            if (c.getId().equals(selectedClientId)) {
                annulerModification();
            }
        } catch (Exception e) {
            addMessage("Erreur", "Une erreur s'est produite lors de la suppression: " + e.getMessage());
        }
    }

    public void annulerModification() {
        // Réinitialiser complètement le client
        this.client = new Client();
        // Assurer que le client sélectionné est également réinitialisé
        this.selectedClientId = null;
        this.selectedClient = null;
        this.clientSelectionne = false;
        this.modeEdition = false;
        //addMessage("Information", "Modification annulée");
    }

    // Méthode pour ajouter des messages
    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    // Getters et Setters
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<Client> getListClients() {
        return listClients;
    }

    public void setListClients(List<Client> listClients) {
        this.listClients = listClients;
    }

    public Long getSelectedClientId() {
        return selectedClientId;
    }

    public void setSelectedClientId(Long selectedClientId) {
        this.selectedClientId = selectedClientId;
    }

    public Client getSelectedClient() {
        return selectedClient;
    }

    public void setSelectedClient(Client selectedClient) {
        this.selectedClient = selectedClient;
    }

    public boolean isModeEdition() {
        return modeEdition;
    }

    public void setModeEdition(boolean modeEdition) {
        this.modeEdition = modeEdition;
    }

    public boolean isClientSelectionne() {
        return clientSelectionne;
    }

    public void setClientSelectionne(boolean clientSelectionne) {
        this.clientSelectionne = clientSelectionne;
    }
    
    // Nouveaux getters et setters pour la recherche
    public String getSearchQuery() {
        return searchQuery;
    }
    
    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }
    
    public List<Client> getFilteredClients() {
        return filteredClients;
    }
    
    public void setFilteredClients(List<Client> filteredClients) {
        this.filteredClients = filteredClients;
    }
}