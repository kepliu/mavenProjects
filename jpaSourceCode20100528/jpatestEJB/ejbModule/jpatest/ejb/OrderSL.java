package jpatest.ejb;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import jpatest.entity.Book;
import jpatest.entity.Customer;
import jpatest.entity.LineItem;
import jpatest.entity.Order;

// Using JNDI lookup to obtain container-managed transaction-scoped entity manager

@Stateless
@PersistenceContext(name = "jpaTestJtaPC", unitName = "jpaTestJtaPU")
public class OrderSL implements OrderSLLocal {
	@Resource
	SessionContext ctx;

	@EJB
	LineItemSLLocal lineItemSLLocal;

	public void createOrder(Order o, Book book) {
		EntityManager em = (EntityManager) ctx.lookup("jpaTestJtaPC");
		em.persist(book);
		em.persist(o); // insert into database
	}

	// retrieve all orders that are placed before a given date
	public List<Order> retrieveOrders(Timestamp orderTime) {
		EntityManager em = (EntityManager) ctx.lookup("jpaTestJtaPC");
		Query query = em.createNamedQuery("queryOrders");
		query.setParameter(1, orderTime, TemporalType.TIMESTAMP);
		List<Order> result = query.getResultList();
		return result;
	}

	public void addLineItem(Order order, LineItem li) {
		EntityManager em = (EntityManager) ctx.lookup("jpaTestJtaPC");
		em.merge(order);
		lineItemSLLocal.addLineItem(order, li);
	}

	public Order addLineItem(int orderID, LineItem li) {
		EntityManager em = (EntityManager) ctx.lookup("jpaTestJtaPC");

		// find the order in in the persistence context or bring it to the
		// persistence context, before we add the line item to it.
		Order order = em.find(Order.class, orderID);
		li.setOrder(order);
		order.getLineItems().add(li);
		return order;
	}

	// force loading of lazy attributes so that they are available after detachment.
	public Order findOrder(int orderID) {
		EntityManager em = (EntityManager) ctx.lookup("jpaTestJtaPC");

		// find the order in in the persistence context or bring it to the
		// persistence context, before we add the line item to it.
		Order order = em.find(Order.class, orderID);

		// if customer is configured to be LAZY, then we need to invoke a
		// non-primary key field to trigger loading
		Customer cust = order.getCustomer();
		if (cust != null) {
			// call non-primary key attribute
			cust.getName(); 
		}

		// to trigger loading on collection of line items
		List<LineItem> lineItems = order.getLineItems();
		if (lineItems != null) {
			// call size() is OK for collection-valued attribute
			lineItems.size();
		}
		return order;
	}

	public void deleteOrder(Order o, Book book) {
		EntityManager em = (EntityManager) ctx.lookup("jpaTestJtaPC");
		em.remove(em.merge(o));
		em.remove(em.merge(book));
	}

	public void createBookAndOrder(double price) {

		Order order = new Order(); // order is in new state
		order.setOrderTime(new Timestamp(new Date().getTime()));
		order.setPrice(price);

		Book book = new Book(); // book is in new state
		book.setIsbn("12345");

		LineItem li = new LineItem(); // li is in new state
		li.setPrice(10.00);
		li.setQuantity(10);
		li.setBook(book); // set relationship to book
		li.setOrder(order); // set relationship to order

		List<LineItem> lineItems = new ArrayList<LineItem>();
		lineItems.add(li);
		order.setLineItem(lineItems);

		EntityManager em = (EntityManager) ctx.lookup("jpaTestJtaPC");
		em.persist(book); // book is in managed state

		// This causes LineItem to be persisted due to cascade policy
		// Otherwise, we would need to call em.persist(li).
		em.persist(order); // order and li are now managed
	}

	// repeatable read by using optimistic locking
	public double repeatableRead(int orderId) {
		EntityManager em = (EntityManager) ctx.lookup("jpaTestJtaPC");
		Order order = em.find(Order.class, orderId);

		// Lock the object to ensure repeatable read
		em.lock(order, LockModeType.READ);

		Double price = order.getPrice();
		return price;
	}

}
