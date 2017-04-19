package jpatest.eao;

import javax.persistence.EntityManager;

import jpatest.entity.Customer;

/**
 * Test for joined table customer inheritance mapping
 *
 */
public class CustomerEAO extends BaseEAO {
	
	/**
	 * Create an array of customer objects, some of which may be of a derived
	 * class from Customer.
	 * @param cs
	 */
	public void createCustomer(Customer[] cs) {
		// create a new instance of entity manager
		EntityManager em = createEntityManager();

		try {
			em.getTransaction().begin(); 
			for (int i = 0; i < cs.length; i++) {
				em.persist(cs[i]); 
			}
			em.getTransaction().commit(); 
		} catch (Throwable t) {
			t.printStackTrace();
			em.getTransaction().rollback(); 
		} finally {
			closeEntityManager(em);
		}
	}

	/**
	 * Update an array of customers into the database and return an array 
	 * of the updated customers.
	 * Notice that version of returned object should be different from the
	 * the object passed in, due to optimistic locking.
	 * 
	 * @param cs
	 * @return
	 */
	public Customer[] updateCustomer(Customer[] cs) {
		EntityManager em = createEntityManager();
		Customer[] cs2 = new Customer[cs.length];
		try {
			em.getTransaction().begin(); 
			for (int i = 0; i < cs.length; i++) {
				cs2[i] = em.merge(cs[i]); 
			}
			em.getTransaction().commit(); 
		} catch (Throwable t) {
			t.printStackTrace();
			em.getTransaction().rollback(); 
		} finally {
			closeEntityManager(em);
		}
		return cs2;
	}

	public void deleteCustomer(Customer[] cs) {
		EntityManager em = createEntityManager();
		try {
			em.getTransaction().begin(); 
			for (int i = 0; i < cs.length; i++) {
				em.remove(em.merge(cs[i])); 
			}
			em.getTransaction().commit(); 
		} catch (Throwable t) {
			t.printStackTrace();
			em.getTransaction().rollback(); 
		} finally {
			closeEntityManager(em);
		}
	}

}
