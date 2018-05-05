package my.risktool.darkwingduck.model;

import java.sql.ResultSet;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class Database {
    
    EntityManagerFactory emf;
    EntityManager em;

    public Database() {
        connectToDatabase();
    }
    
    public EntityManager getEm() {
        return em;
    }
    
    public void connectToDatabase() {
        if (emf == null) {
            emf = javax.persistence.Persistence.createEntityManagerFactory("my.risktool.darkwingduck_PU");
        } 
        
        if (em == null) {
            em = emf.createEntityManager();
        }   
    }
    
    public ResultSet executeQuery() {
        return null;
    }
}
