package jpatest.web.eao;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * Base EAO for application-managed entity manager and resource-local 
 * transaction.
 */
public class BaseEAO2 {
	public static EntityManager createEntityManager() {
		InitialContext ctx = null;
		EntityManagerFactory emf = null;
		try {
			ctx = new InitialContext();
			
			// persistence unit jpaTestPUnit is defined in calling Servlet or web.xml
			emf = (EntityManagerFactory) ctx.lookup("java:comp/env/jpaTestPUnit");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return emf.createEntityManager();
	}
	
	public static void closeEntityManager(EntityManager em) {
		if (em != null && em.isOpen()) {
			em.close();
		}
	}
}
