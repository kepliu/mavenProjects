package jpatest.eao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.Query;

import jpatest.entity.LineItem;
import jpatest.entity.Order;


public class OrderEAO extends BaseEAO {

	public void createOrder(Order order) {
		EntityManager em = createEntityManager();

		try {
			em.getTransaction().begin();
			em.persist(order);                 // cascade to associated line items
			em.getTransaction().commit();
		} catch (Throwable t) {
			t.printStackTrace();
			em.getTransaction().rollback();
		} finally {
			closeEntityManager(em);
		}
	}

	public Order updateOrder(Order order) {
		EntityManager em = createEntityManager();
		Order orderNew = null;
		try {
			em.getTransaction().begin();
			orderNew = em.merge(order);
			em.getTransaction().commit();
		} catch (Throwable t) {
			t.printStackTrace();
			em.getTransaction().rollback();
		} finally {
			closeEntityManager(em);
		}
		return orderNew;
	}

	public Order addLineItem(Order order, LineItem li) {
		EntityManager em = createEntityManager();
		Order  o = null;
		try {
			em.getTransaction().begin();
			o = em.merge(order);
			o.getLineItems().add(li);
			li.setOrder(o);
			em.getTransaction().commit();
		} catch (Throwable t) {
			t.printStackTrace();
			em.getTransaction().rollback();
		} finally {
			closeEntityManager(em);
		}
		
		return o;
	}

	public void addLineItem(int orderId, int quantity, double price) {
		EntityManager em = createEntityManager();
		try {
			em.getTransaction().begin();

			Map<String, Object> props = new HashMap<String, Object>();
			props.put("javax.persistence.lock.timeout", 1000);
			// Order order = em.find(Order.class, orderId,
			// LockModeType.PESSIMISTIC_READ, props);

			Order order = em.find(Order.class, orderId,
					LockModeType.OPTIMISTIC_FORCE_INCREMENT, props);
			if (order == null) return;
			
			Timestamp t = order.getOrderTime();
			if (t.before(new Date())) {
				double pric = order.getPrice();
			}

			LineItem li = new LineItem();
			li.setQuantity(quantity);
			li.setPrice(price);
			li.setOrder(order);
			em.persist(li);
			order.getLineItems().add(li);	
			em.getTransaction().commit();
		} catch (Throwable t) {
			t.printStackTrace();
			em.getTransaction().rollback();
		} finally {
			closeEntityManager(em);
		}
	}

	public void performDiscount(int orderId, double percentage) {
		EntityManager em = createEntityManager();
		try {
			em.getTransaction().begin();
			Order order = em.find(Order.class, orderId);
			List<LineItem> items = order.getLineItems();
			double price = 0;
			for (LineItem i : items) {
				price += i.getPrice();
			}
			price *= 0.2;
			order.setPrice(price);
			em.getTransaction().commit();
		} catch (Throwable t) {
			t.printStackTrace();
			em.getTransaction().rollback();
		} finally {
			closeEntityManager(em);
		}
	}

	public Order createLineItem(Order o, int quantity, double price) {
		EntityManager em = createEntityManager();
		LineItem li = null;
		try {
			em.getTransaction().begin();
			o = em.merge(o);
			li = new LineItem();
			li.setQuantity(quantity);
			li.setPrice(price);
			li.setOrder(o);
			List<LineItem> items = o.getLineItems();
			if (items == null) {
				// added on 2010-03-07 for Hibernate. 
				// EclipseLink does not need this check
				items = new ArrayList<LineItem>();		
			}
			items.add(li);
			o.setLineItem(items);
			em.persist(li);
			em.getTransaction().commit();
		} catch (Throwable t) {
			t.printStackTrace();
			em.getTransaction().rollback();
		} finally {
			closeEntityManager(em);
		}
		return o;
	}

	public LineItem createLineItem(int quantity, double price) {
		EntityManager em = createEntityManager();
		LineItem li = null;
		try {
			em.getTransaction().begin();
			li = new LineItem();
			li.setQuantity(quantity);
			li.setPrice(price);
			em.persist(li);
			em.getTransaction().commit();
		} catch (Throwable t) {
			t.printStackTrace();
			em.getTransaction().rollback();
		} finally {
			closeEntityManager(em);
		}
		return li;
	}

	public void deleteLineItems(Collection<LineItem> lineItems) {
		EntityManager em = createEntityManager();
		try {
			em.getTransaction().begin();    
			for (LineItem i : lineItems) {
				System.out.println("deleting line item: " + i.getLineItemId());
				em.remove(em.merge(i));      // merge into managed state & remove
			}
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
			// due to cascade remove configured,
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
	
	public void deleteOrder(Order[] orders) {
		EntityManager em = createEntityManager();
		try {
			// due to cascade remove configured,
			// line items are automatically deleted
			em.getTransaction().begin();
			for (Order o: orders){
			  em.remove(em.merge(o));
			}
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

			System.out.println("order with max orderId : " + order.getPrice());
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			closeEntityManager(em);
		}

		return order;
	}

}
