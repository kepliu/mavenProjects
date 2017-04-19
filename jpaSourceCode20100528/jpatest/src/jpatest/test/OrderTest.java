package jpatest.test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.Query;


import org.junit.Test;

import jpatest.eao.BookCategoryEAO;
import jpatest.eao.CustomerEAO;
import jpatest.eao.OrderEAO;
import jpatest.entity.BankInfo;
import jpatest.entity.Book;
import jpatest.entity.Category;
import jpatest.entity.Customer;
import jpatest.entity.LineItem;
import jpatest.entity.Order;
import jpatest.entity.OrderStatus;

public class OrderTest extends MyJpaTestCase {
	@Test
	public void testOrderEAO() {

		Object[] created = createOrder(em);
		Order[] ordersCreated = (Order[]) created[0];
		Customer[] custCreated = (Customer[]) created[1];
		List<Book> booksCreated = (List<Book>) created[2];

		Order[] orderUpdated = updateOrder(em, ordersCreated, custCreated,
				booksCreated);

		deleteOrder(em, orderUpdated, custCreated, booksCreated);

		// test addLineItem methods in OrderEAO
		addLineItem();
	}

	protected static Object[] createOrder(EntityManager em) {
		Order order = new Order();
		order.setOrderTime(new Timestamp(new Date().getTime() - 10000));
		order.setPrice(100.30);
		order.setStatus(OrderStatus.BILLED);

		Customer customer = new Customer();
		customer.setName("John Customer");
		customer.setPicture("some picture".getBytes());
		customer.setIncome(100000.0);
		BankInfo bank = new BankInfo();
		bank.setBankName("BND");
		customer.setBank(bank);
		order.setCustomer(customer);

		LineItem li = new LineItem();
		li.setPrice(10.00);
		li.setQuantity(10);
		// LineItem is owner of order-LineItem relationship
		// Call this setter is necessary
		li.setOrder(order);

		Book book = new Book();
		book.setIsbn("isbn #");
		book.setTitle("book Title");
		book.setPdf("some content".getBytes());

		LineItem li2 = new LineItem();
		li2.setPrice(5.00);
		li2.setQuantity(30);
		li2.setBook(book);
		li2.setOrder(order);
		li.setBook(book);

		// This will ensure cascade persist
		List<LineItem> lineItems = new ArrayList<LineItem>();
		lineItems.add(li);
		lineItems.add(li2);
		order.setLineItem(lineItems);
		book.setLineItems(lineItems);

		Order order2 = new Order();
		order2.setOrderTime(new Timestamp(new Date().getTime() - 10000));
		order2.setPrice(200.50);
		order2.setStatus(OrderStatus.BILLED);

		Map<Integer, Order> orders = new HashMap<Integer, Order>();
		orders.put(new Integer(1), order);
		Map<Integer, Order> orders2 = new HashMap<Integer, Order>();
		orders2.put(new Integer(1), order2);

		Customer customer2 = new Customer();
		customer2.setName("John Customer2");
		customer2.setPicture("some picture".getBytes());
		customer2.setIncome(20000.0);
		BankInfo bank2 = new BankInfo();
		bank2.setBankName("BND2");
		customer2.setBank(bank2);
		order2.setCustomer(customer2);
		
		customer.setOrders(orders);
		customer2.setOrders(orders2);
		
		em.getTransaction().begin();
		em.persist(book);
		em.persist(order);
		em.persist(order2);
		em.persist(customer);
		em.persist(customer2);	
		em.getTransaction().commit();
		
		// create books
		BookCategoryEAO bookEao = new BookCategoryEAO();
		List<Book> books = new ArrayList<Book>();
		books.add(book);
		// bookEao.createBookCategory(books, new ArrayList<Category>());
		//		

		// create order
		OrderEAO orderEao = new OrderEAO();
		// orderEao.createOrder(order); // not needed, since customer persist
		// will cascade to orders
		// orderEao.createOrder(order2);

		// create customer
		CustomerEAO custEao = new CustomerEAO();
		// custEao.createCustomer(new Customer[] { customer, customer2 });

		/**
		 * Test the correctness of the create operations
		 */
//		Book bo = em.find(Book.class, book.getBookId());
//		List<LineItem> boLIs = bo.getLineItems();
//		System.out.println("\nBook has LineItems: " + boLIs.size());

		Query query = em
				.createQuery("SELECT b FROM Book b WHERE b.bookId = ?1");
		query.setParameter(1, book.getBookId());
		query.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
		Book b2 = (Book) query.getSingleResult();
		List<LineItem> b2LIs = b2.getLineItems();
		System.out.println("\nBook has LineItems: " + b2LIs.size());
		assertTrue(2 == b2LIs.size());
		for (LineItem i : b2LIs) {
			Book b = i.getBook();
			Order o = i.getOrder();
			Customer c = o.getCustomer();
			String name = null;
			if (c != null)
				name = c.getName();
			System.out.println("\nLineItem " + i.getLineItemId() + " for "
					+ b.getIsbn() + " is part of order with price:"
					+ o.getPrice() + " for customer: " + name);

			// Set<LineItem> orLIs = order.getLineItems();
			// System.out.println("\nOrder has LineItems: " + orLIs.size());
		}

		Order[] createdOrders = new Order[] { order, order2 };
		Customer[] createdCust = new Customer[] { customer, customer2 };
		Object[] objs = new Object[] { createdOrders, createdCust, books };
		
		return objs;
	}

	protected static  Order[] updateOrder(EntityManager em, Order[] orders,
			Customer[] cust, List<Book> books) {
		Order order = orders[0];
		order.setPrice(1000.00);
		order.setOrderTime(new Timestamp(new Date().getTime() - 20000));
		List<LineItem> LIs = order.getLineItems();

		// create a new line item for the order
		em.getTransaction().begin();
		LineItem li = new LineItem();
		li.setPrice(100.00);
		li.setQuantity(100);
		li.setBook(books.get(0));     
		li.setOrder(order); 
		em.persist(li);
		em.getTransaction().commit();

		List<LineItem> lineItems = new ArrayList<LineItem>();
		lineItems.add(li);
		lineItems.addAll(LIs);
		order.setLineItem(lineItems);

		// update order
		OrderEAO orderEao = new OrderEAO();
		Order o = orderEao.updateOrder(order);
		orders[0] = o;
	
		return orders;
	}

	protected  static void deleteOrder(EntityManager em, Order[] orders,
			Customer[] custs, List<Book> books) {
		
		OrderEAO orderEao = new OrderEAO();
		orderEao.deleteOrder(orders);
		
		// Hibernate insists that I set the orders to null before I can delete
		// customer. EclipseLink does not care and works either way
		for (Customer c : custs) {
			c.setOrders(null);
		}
		
		CustomerEAO custEao = new CustomerEAO();
		custEao.deleteCustomer(custs);

		// Hibernate insists that I set the lineItems to null before I can
		// delete book. EclipseLink does not care and works either way
		for (Book b : books) {
			b.setLineItems(null);
		}

		BookCategoryEAO bcEao = new BookCategoryEAO();
		List<Category> categories = new ArrayList<Category>();
		bcEao.deleteBookCategories(books, categories);

	}

	protected void addLineItem() {
		em.clear();
		System.out.println("test addLineItem");

		Order o2 = new Order();
		o2.setOrderTime(new Timestamp(new Date().getTime() - 10000));
		o2.setPrice(100.00);
		o2.setStatus(OrderStatus.BILLED);

		OrderEAO orderEao = new OrderEAO();
		orderEao.createOrder(o2);
		o2 = orderEao.createLineItem(o2, 5, 34.99);

		LineItem item = new LineItem();
		item.setPrice(1.3);
		item.setQuantity(9);
		o2 = orderEao.addLineItem(o2, item);
		orderEao.addLineItem(o2.getOrderId(), 7, 5.99);
		System.out.println("order id 1= " + o2.getOrderId());

		em.getTransaction().begin();
		o2 = em.find(Order.class, o2.getOrderId());

		// EclipseLink and Hibernate behaves differently here.
		if (o2 != null) {
			// EclipseLink 2.0.0 can find this object although it is persisted
			// in a different persistence context in orderEao.
			System.out.println("order id 2= " + o2.getOrderId());
		} else {
			// Hibernate 3.5.0 returns o2 as null because it cannot find it in 
			// the persistence context represented by the em variable.
			System.out.println("o2 is null");
		}

		if (o2 != null) {
			em.remove(o2);
		} 
		em.getTransaction().commit();
	}

}
