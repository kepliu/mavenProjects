package jpatest.eao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.LockModeType;
import javax.persistence.Persistence;

import jpatest.entity.LineItem;
import jpatest.entity.Order;


/**
 * This example is not recommended since it is not thread-safe. Also managed
 * entity objects remain managed after a method is finished, and may be shared
 * by different users/threads.
 * 
 */
public class OrderEAO2 {

	EntityManagerFactory emf = null;
	EntityManager em = null;

	public OrderEAO2() {
		emf = Persistence.createEntityManagerFactory("jpaTestPU");
		em = emf.createEntityManager();
	}

	public void createOrder(Order order) {
		try {
			em.getTransaction().begin();
			em.persist(order);
			em.getTransaction().commit();
		} catch (Throwable t) {
			t.printStackTrace();
			em.getTransaction().rollback();
		}
	}

	public void addLineItem(int orderId, int quantity, double price) {
		try {
			em.getTransaction().begin();
			Order order = em.find(Order.class, orderId);
			LineItem li = new LineItem();
			li.setQuantity(quantity);
			li.setPrice(price);
			li.setOrder(order);
			order.getLineItems().add(li);
			em.getTransaction().commit();
		} catch (Throwable t) {
			t.printStackTrace();
			em.getTransaction().rollback();
		}
	}

	public void release() {
		if (em != null && em.isOpen()) {
			em.close();
		}

		if (emf != null && emf.isOpen()) {
			emf.close();
		}
	}

}
