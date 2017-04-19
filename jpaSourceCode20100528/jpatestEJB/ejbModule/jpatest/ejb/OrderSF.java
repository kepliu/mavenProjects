package jpatest.ejb;

import java.sql.Timestamp;
import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import jpatest.entity.Book;
import jpatest.entity.LineItem;
import jpatest.entity.Order;

//Using dependency injection to obtain container-managed extended entity manager

@Stateful
// @Transaction(REQUIRES_NEW)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class OrderSF implements OrderSFLocal {

	@PersistenceContext(type = PersistenceContextType.EXTENDED, unitName = "jpaTestJtaPU")
	EntityManager em;

	private Order order;
	private Book book;

	public void init(Order order, Book book) {
		this.order = order;
		this.book = book;
	}

	public void createOrder() {
		em.persist(book);
		em.persist(order);
	}

	// retrieve all orders that are placed before a given date
	public List<Order> retrieveOrders(Timestamp orderTime) {
		Query query = em.createNamedQuery("queryOrders");
		query.setParameter(1, orderTime, TemporalType.TIMESTAMP);
		List<Order> result = query.getResultList();
		
		// The list of orders remain managed in this extended persistent context
	    // even after this method returns
		return result;
	}
	
	public void addLineItem(LineItem li) {
		li.setOrder(order);
		order.getLineItems().add(li);	
	}

	@Remove
	public void deleteOrder() {
		// There is no need to merge order and book since they remain managed
		// in this extended persistence context.
		em.remove(order);
		em.remove(book);
	}
	
}
