package com.mycompany.myapp.bean;

import com.mycompany.myapp.dao.ClientDAO;
import com.mycompany.myapp.model.Client;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
public class SearchBean implements Serializable {

    @Inject 
    private ClientDAO clientDAO;
    
    private String query;
    private List<Client> searchResults;
    
    public void search() {
        if (query == null || query.trim().isEmpty()) {
            // Si la recherche est vide, réinitialiser les résultats
            searchResults = null;
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Attention", "Veuillez saisir un terme de recherche"));
            return;
        }
        
        searchResults = clientDAO.searchByNomOrPrenom(query.trim());
        
        if (searchResults.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Information", 
                            "Aucun client trouvé pour: \"" + query + "\""));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Résultats", 
                            searchResults.size() + " client(s) trouvé(s)"));
        }
    }
    
    public String getQuery() {
        return query;
    }
    
    public void setQuery(String query) {
        this.query = query;
    }
    
    public List<Client> getSearchResults() {
        return searchResults;
    }
    
    public void setSearchResults(List<Client> searchResults) {
        this.searchResults = searchResults;
    }
    
    public boolean hasResults() {
        return searchResults != null && !searchResults.isEmpty();
    }
} 