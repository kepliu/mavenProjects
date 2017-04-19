package jpatest.web.eao;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import jpatest.entity.LineItem;
import jpatest.entity.Order;

/**
 * Using container-managed transaction and entity manager in a Servlet
 * container. JTA data source is required for container-managed transaction.
 * 
 */
public class OrderEAO extends BaseEAO {

	public void createOrder(Order order) {
		UserTransaction ut = getUserTransaction();

		try {
			ut.begin();
			EntityManager em = getEntityManager();
			em.persist(order);     // cascade to associated line items
			ut.commit();
		} catch (Throwable e) {
			e.printStackTrace();
			try {
				ut.rollback();
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}

	public void addLineItem(Order order, LineItem li) {
		UserTransaction ut = getUserTransaction();
		try {
			ut.begin();
			EntityManager em = getEntityManager();
			em.joinTransaction();
			em.merge(order);
			order.getLineItems().add(li);
			ut.commit();
		} catch (Throwable e) {
			e.printStackTrace();
			try {
				ut.rollback();
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}

	public void deleteOrder(Order order) {
		UserTransaction ut = getUserTransaction();

		try {
			ut.begin();
			EntityManager em = getEntityManager();
			em.joinTransaction();
			em.remove(em.merge(order));    // cascade to associated line items
			ut.commit();
		} catch (Throwable e) {
			e.printStackTrace();
			try {
				ut.rollback();
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}

	public Order queryOrderWithMaxId() {
		Order order = null;
		EntityManager em = getEntityManager();

		Query query = em.createQuery("SELECT o FROM Order o "
				+ "WHERE o.orderId = (SELECT MAX(o2.orderId) FROM Order o2)");
		order = (Order) query.getSingleResult();

		// System.out.println("order price with max orderId : " +
		// order.getPrice());

		return order;
	}

}