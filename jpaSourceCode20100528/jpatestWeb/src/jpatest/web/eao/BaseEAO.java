package jpatest.web.eao;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

/**
 * Base EAO for container-managed entity manager and JTA transaction
 */
public class BaseEAO {
	public static EntityManager getEntityManager() {
		EntityManager em = null;
		try {
			InitialContext ctx = new InitialContext();
			
			// persistence context jpaTestJtaPC is defined in calling Servlet or web.xml
			em = (EntityManager) ctx.lookup("java:comp/env/jpaTestJtaPC");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return em;
	}

	/**
	 * Get the UserTransaction in the container managed JNDI tree.
	 */
	public static UserTransaction getUserTransaction() {
		UserTransaction ut = null;
		try {
			InitialContext context = new InitialContext();
			ut = (UserTransaction) context.lookup("java:comp/UserTransaction");
		} catch (NamingException e) {
			e.printStackTrace();
		}

		return ut;
	}
}
