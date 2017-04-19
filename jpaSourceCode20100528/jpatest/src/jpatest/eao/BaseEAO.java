package jpatest.eao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class BaseEAO {
	static final EntityManagerFactory EMF = Persistence
			.createEntityManagerFactory("jpaTestPU");

	public static EntityManager createEntityManager() {
		return EMF.createEntityManager();
	}

	public static void closeEntityManager(EntityManager em) {
		if (em != null && em.isOpen()) {
			em.close();
		}
	}
	
	public static EntityManagerFactory getEntityManagerFactory() {
		return EMF;
	}

	public static void create(Object entity) {
		EntityManager em = createEntityManager();

		try {
			em.getTransaction().begin(); 
			em.persist(entity); 
			em.getTransaction().commit(); 
		} catch (Throwable t) {
			t.printStackTrace();
			em.getTransaction().rollback(); 
		} finally {
			em.close(); 
		}
	}

	public static Object update(Object entity) {
		EntityManager em = createEntityManager();
		Object updated = null;
		try {
			em.getTransaction().begin(); 
			updated = em.merge(entity); 
			em.getTransaction().commit(); 
		} catch (Throwable t) {
			t.printStackTrace();
			em.getTransaction().rollback(); 
		} finally {
			em.close(); 
		}
		return updated;
	}

	public static void delete(Object entity) {
		EntityManager em = createEntityManager();
		try {
			em.getTransaction().begin(); 
			em.remove(em.merge(entity)); 
			em.getTransaction().commit(); 
		} catch (Throwable t) {
			t.printStackTrace();
			em.getTransaction().rollback(); 
		} finally {
			em.close(); 
		}
	}

	// /**
	// * Get the UserTransaction in the container managed JNDI tree.
	// */
	// public UserTransaction getUserTransaction() {
	// UserTransaction ut = null;
	// try {
	// InitialContext context = new InitialContext();
	// ut = (UserTransaction) context.lookup("java:comp/UserTransaction");
	// } catch (NamingException e) {
	// e.printStackTrace();
	// }
	//
	// return ut;
	// }

}
