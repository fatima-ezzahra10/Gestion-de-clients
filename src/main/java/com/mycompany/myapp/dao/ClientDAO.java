package com.mycompany.myapp.dao;

import com.mycompany.myapp.model.Client;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless
public class ClientDAO {
    
    @PersistenceContext(unitName = "formClientPU")
    private EntityManager em;
    
    public void create(Client client) {
        em.persist(client);
    }
    
    public void update(Client client) {
        em.merge(client);
    }
    
    public void delete(Client client) {
        if (!em.contains(client)) {
            client = em.merge(client);
        }
        em.remove(client);
    }
    
    public Client findById(Long id) {
        return em.find(Client.class, id);
    }
    
    public List<Client> findAll() {
        return em.createQuery("SELECT c FROM Client c", Client.class).getResultList();
    }
    
     public List<Client> searchByNomOrPrenom(String searchTerm) {
        String searchPattern = "%" + searchTerm.toLowerCase() + "%";
        TypedQuery<Client> query = em.createQuery(
                "SELECT c FROM Client c WHERE LOWER(c.nom) LIKE :searchTerm OR LOWER(c.prenom) LIKE :searchTerm", 
                Client.class);
        query.setParameter("searchTerm", searchPattern);
        return query.getResultList();
    }
}
