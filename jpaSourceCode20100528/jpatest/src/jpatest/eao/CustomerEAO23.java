package jpatest.eao;

import javax.persistence.EntityManager;

import jpatest.entity.Customer2;
import jpatest.entity.Customer3;


/**
 * Test for single table customer inheritance mapping and
 * table per class inheritance mapping
 *
 */
public class CustomerEAO23 extends BaseEAO  {

	/**
	 * Create an array of customer2 objects, some of which may be of a derived
	 * class from Customer2.
	 * @param cs
	 */
	public void createCustomer(Customer2[] cs) {
		// create a new instance of entity manager
		EntityManager em = createEntityManager();

		try {
			em.getTransaction().begin(); // start transaction
			for (int i = 0; i < cs.length; i++) {
				em.persist(cs[i]); // insert into database
			}
			em.getTransaction().commit(); // commit transaction
		} catch (Throwable t) {
			t.printStackTrace();
			em.getTransaction().rollback(); // rollback transaction
		} finally {
			em.close(); // close entity manager
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
	public Customer2[] updateCustomer(Customer2[] cs) {
		EntityManager em = createEntityManager();
		Customer2[] cs2 = new Customer2[cs.length];
		try {
			em.getTransaction().begin(); // start transaction
			for (int i = 0; i < cs.length; i++) {
				cs2[i] = em.merge(cs[i]); // merge new values into database
			}
			em.getTransaction().commit(); // commit transaction
		} catch (Throwable t) {
			t.printStackTrace();
			em.getTransaction().rollback(); // rollback transaction
		} finally {
			em.close(); // close entity manager
		}
		return cs2;
	}

	public void deleteCustomer(Customer2[] cs) {
		EntityManager em = createEntityManager();
		try {
			em.getTransaction().begin(); // start transaction
			for (int i = 0; i < cs.length; i++) {
				em.remove(em.merge(cs[i])); // merge into managed state & remove
			}
			em.getTransaction().commit(); // commit transaction
		} catch (Throwable t) {
			t.printStackTrace();
			em.getTransaction().rollback(); // rollback transaction
		} finally {
			em.close(); // close entity manager
		}
	}
	
	
	/**
	 * Create an array of customer3 objects, some of which may be of a derived
	 * class from Customer2.
	 * @param cs
	 */
	public void createCustomer(Customer3[] cs) {
		// create a new instance of entity manager
		EntityManager em = createEntityManager();

		try {
			em.getTransaction().begin(); // start transaction
			for (int i = 0; i < cs.length; i++) {
				em.persist(cs[i]); // insert into database
			}
			em.getTransaction().commit(); // commit transaction
		} catch (Throwable t) {
			t.printStackTrace();
			em.getTransaction().rollback(); // rollback transaction
		} finally {
			em.close(); // close entity manager
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
	public Customer3[] updateCustomer(Customer3[] cs) {
		EntityManager em = createEntityManager();
		Customer3[] cs2 = new Customer3[cs.length];
		try {
			em.getTransaction().begin(); // start transaction
			for (int i = 0; i < cs.length; i++) {
				cs2[i] = em.merge(cs[i]); // merge new values into database
			}
			em.getTransaction().commit(); // commit transaction
		} catch (Throwable t) {
			t.printStackTrace();
			em.getTransaction().rollback(); // rollback transaction
		} finally {
			em.close(); // close entity manager
		}
		return cs2;
	}

	public void deleteCustomer(Customer3[] cs) {
		EntityManager em = createEntityManager();
		try {
			em.getTransaction().begin(); // start transaction
			for (int i = 0; i < cs.length; i++) {
				em.remove(em.merge(cs[i])); // merge into managed state & remove
			}
			em.getTransaction().commit(); // commit transaction
		} catch (Throwable t) {
			t.printStackTrace();
			em.getTransaction().rollback(); // rollback transaction
		} finally {
			em.close(); // close entity manager
		}
	}

}
