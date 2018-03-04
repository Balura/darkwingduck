package my.risktool.darkwingduck.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import my.risktool.darkwingduck.model.ThreatCategory;

public class ThreatCategoryDao implements GenericDao<ThreatCategory, String> {

	private Session currentSession;
	
	private Transaction currentTransaction;

	public ThreatCategoryDao() {
	}

	public Session openCurrentSession() {
		currentSession = getSessionFactory().openSession();
		return currentSession;
	}

	public Session openCurrentSessionwithTransaction() {
		currentSession = getSessionFactory().openSession();
		currentTransaction = currentSession.beginTransaction();
		return currentSession;
	}
	
	public void closeCurrentSession() {
		currentSession.close();
	}
	
	public void closeCurrentSessionwithTransaction() {
		currentTransaction.commit();
		currentSession.close();
	}
	
	private static SessionFactory getSessionFactory() {
//		Configuration configuration = new Configuration().configure();
//		StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
//				.applySettings(configuration.getProperties());
//		SessionFactory sessionFactory = configuration.buildSessionFactory(builder.build());
//		return sessionFactory;
		SessionFactory sessionFactory = null;
		
		final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
		        .configure() // configures settings from hibernate.cfg.xml
		        .build();
		try {
			sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
		    
		}
		catch (Exception e) {
		    // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
		    // so destroy it manually.
		    StandardServiceRegistryBuilder.destroy( registry );
		}
		
		return sessionFactory;
	}

	public Session getCurrentSession() {
		return currentSession;
	}

	public void setCurrentSession(Session currentSession) {
		this.currentSession = currentSession;
	}

	public Transaction getCurrentTransaction() {
		return currentTransaction;
	}

	public void setCurrentTransaction(Transaction currentTransaction) {
		this.currentTransaction = currentTransaction;
	}

	public void persist(ThreatCategory entity) {
		getCurrentSession().save(entity);
	}

	public void update(ThreatCategory entity) {
		getCurrentSession().update(entity);
	}

	public ThreatCategory findById(int id) {
		ThreatCategory threat = (ThreatCategory) getCurrentSession().get(ThreatCategory.class, id);
		return threat; 
	}
	
	public boolean findByThreat(String threat) {
		List<ThreatCategory> threats = (List<ThreatCategory>) getCurrentSession().createQuery("FROM ThreatCategory t WHERE t.threat='" + threat + "'").list();
		System.out.println(threats);
		if(threats.isEmpty()) {
			return true;
		} else {
			return false;
		}
		
	}

	public void delete(ThreatCategory entity) {
		getCurrentSession().delete(entity);
	}

	@SuppressWarnings("unchecked")
	public List<ThreatCategory> findAll() {
		List<ThreatCategory> threats = (List<ThreatCategory>) getCurrentSession().createQuery("from ThreatCategory").list();
		return threats;
	}
	
	public List<ThreatCategory> findAllThreats() {
		List<ThreatCategory> threats = (List<ThreatCategory>) getCurrentSession().createQuery("SELECT id from ThreatCategory").list();
		return threats;
	}
	
	public void deleteAll() {
		List<ThreatCategory> entityList = findAll();
		for (ThreatCategory entity : entityList) {
			delete(entity);
		}
	}
	
}
