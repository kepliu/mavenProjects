package jpatest.ejb.eao;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.interceptor.Interceptors;

import jpatest.entity.LineItem;
import jpatest.entity.Order;

/**
 * An example EAO with application-managed entity manager and bean-managed 
 * resource-local transaction.
 */
@Stateless(mappedName = "OrderEAO2")
@TransactionManagement(TransactionManagementType.BEAN)
//@Interceptors({jpatest.ejb.util.RequestContextInterceptor.class})
public class OrderEAO2 implements OrderEAO2Local {
	@PersistenceUnit(unitName = "jpaTestPU")
	private EntityManagerFactory emf;

	public void createOrder(Order order) {
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			em.persist(order);
			em.getTransaction().commit();
		} catch (Throwable t) {
			t.printStackTrace();
			em.getTransaction().rollback();
		} finally {
			em.close();
		}
	}
	
	public void addLineItem(Order order, LineItem li) {	
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			em.merge(order);
			order.getLineItems().add(li);
			em.getTransaction().commit();
		} catch (Throwable t) {
			t.printStackTrace();
			em.getTransaction().rollback();
		} finally {
			em.close();
		}
	}

	public void deleteOrder(Order order) {
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			em.remove(em.merge(order));
			em.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			em.getTransaction().rollback();
		} finally {
			em.close();
		}
	}

	public Order queryOrderWithMaxId() {
		Order order = null;
		EntityManager em = emf.createEntityManager();
		try {
			Query query = em
					.createQuery("SELECT o FROM Order o "
							+ "WHERE o.orderId = (SELECT MAX(o2.orderId) FROM Order o2)");
			order = (Order) query.getSingleResult();
		} finally {
			em.close();
		}
		return order;
	}
}
