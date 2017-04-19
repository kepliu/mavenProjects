package jpatest.web.eao;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import jpatest.entity.LineItem;
import jpatest.entity.Order;

/**
 * Using application-managed entity manager and resource-local transaction in a
 * Servlet container. Resource-local data source is required in this case.
 * 
 */
public class OrderEAO2 extends BaseEAO2 {

	public void createOrder(Order order) {
		EntityManager em = createEntityManager();
		try {
			// due to cascade persist configuration,
			// line items are automatically inserted
			em.getTransaction().begin();
			em.persist(order);
			em.getTransaction().commit();
		} catch (Throwable t) {
			t.printStackTrace();
			em.getTransaction().rollback();
		} finally {
			closeEntityManager(em);
		}
	}

	public void addLineItem(Order order, LineItem li) {
		EntityManager em = createEntityManager();
		try {
			em.getTransaction().begin();
			em.merge(order);
			order.getLineItems().add(li);
			em.getTransaction().commit();
		} catch (Throwable t) {
			t.printStackTrace();
			em.getTransaction().rollback();
		} finally {
			closeEntityManager(em);
		}
	}

	public void deleteOrder(Order order) {
		EntityManager em = createEntityManager();
		try {
			// due to cascade remove configuration,
			// line items are automatically deleted
			em.getTransaction().begin();
			em.remove(em.merge(order));
			em.getTransaction().commit();
		} catch (Throwable t) {
			t.printStackTrace();
			em.getTransaction().rollback();
		} finally {
			closeEntityManager(em);
		}
	}

	public Order queryOrderWithMaxId() {
		Order order = null;
		EntityManager em = createEntityManager();
		try {
			Query query = em
					.createQuery("SELECT o FROM Order o "
							+ "WHERE o.orderId = (SELECT MAX(o2.orderId) FROM Order o2)");
			order = (Order) query.getSingleResult();

			// System.out.println("order with max orderId : " +
			// order.getPrice());
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			closeEntityManager(em);
		}

		return order;
	}

}