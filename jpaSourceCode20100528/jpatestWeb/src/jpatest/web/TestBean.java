package jpatest.web;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sql.DataSource;
import javax.transaction.UserTransaction;

import jpatest.Vehicle;
import jpatest.ejb.BookSFLocal;
import jpatest.ejb.BookSL2Local;
import jpatest.ejb.BookSLLocal;
import jpatest.ejb.OrderSFLocal;
import jpatest.ejb.OrderSLLocal;
import jpatest.ejb.VehicleSLLocal;
import jpatest.ejb.eao.OrderEAO2Local;
import jpatest.ejb.eao.OrderEAOLocal;
import jpatest.entity.Book;
import jpatest.entity.Category;
import jpatest.entity.LineItem;
import jpatest.entity.Order;

public class TestBean {

	@EJB
	protected VehicleSLLocal vehLocal;

	@EJB
	protected OrderSLLocal orderSLLocal;

	@EJB
	protected OrderSFLocal orderSFLocal;

	@EJB
	protected BookSLLocal bookSLLocal;

	@EJB
	protected BookSL2Local bookSL2Local;

	@EJB
	protected BookSFLocal bookSFLocal;

	@EJB
	protected OrderEAOLocal orderEaoLocal;

	@EJB
	protected OrderEAO2Local orderEao2Local;

	String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		System.out.println("input text is: " + text);
		this.text = text;
	}

	public String getMsg() {
		return "Hi, there";
	}

	// A JSF action method on a button click submit
	public String doSubmit() {
		System.out.println("doSubmit is called");
		try {
			// test configuration of data sources
			testResources();
			
			// test Vehicle operations using local and EJB transactions
			testVehicleEJB();
			//testVehicleNonJta();
			
			// test  using stateless and stateful EJBs
			testOrderStatelessEJB();
			testOrderStatefulEJB();

			// test application managed entity manager with container-managed
			// transaction
		    testBookStatelessEJB();
            
		    // test using application-managed entity manager 
		    // with container-managed transaction.
			testBookStatelessEJB2();

			// test using application-managed entity manager 
		    // with container-managed transaction in stateful session bean
			testBookStatefulEJB();

			// test EJB EAOs with JTA and non-JTA transactions
			// testOrderEJB_EAOs();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "same";
	}

	private void testVehicleNonJta() {
		System.out.println("Calling nonJta datasource pu");
		String vinPrimaryKey = "2B7HF16Y7SS244324";
		Vehicle v = new Vehicle(vinPrimaryKey, "Ford", "Flex", 2009);

		EntityManagerFactory EMF = Persistence
				.createEntityManagerFactory("jpaTestPU");
		EntityManager em = EMF.createEntityManager();

		em.getTransaction().begin();  // start transaction
		em.persist(v);                // insert into database
		em.getTransaction().commit(); // commit transaction

		em.getTransaction().begin();  // start transaction
		Vehicle v2 = em.merge(v);     // merge v into managed state
		em.remove(v2);                // delete v from database
		em.getTransaction().commit(); // commit transaction

	}

	private void testVehicleEJB() {
		System.err.println("Calling Jta datasource pu");
		try {
			String vinPrimaryKey = "3B7HF16Y7SS244324";
			Vehicle v = new Vehicle(vinPrimaryKey, "Ford", "Flex", 2009);

			// create a new vehicle and insert it into database
			vehLocal.createVehicle(v);
			assert v.getVersion() >= 0 : "Version error";

			// after update, version should be incremented by 1
			v.setMake("Mercury");
			Vehicle v2 = vehLocal.updateVehicle(v);
			assert v2.getVersion() == v.getVersion() + 1 : "Optimistic lock error";

			// retrieve all vehicles with make "Mercury"
			List<Vehicle> vehList = vehLocal.retrieveVehicles("Mercury");
			for (Vehicle veh: vehList) {
				System.out.println("retrieved vehicle: " + veh.getMake());
			}

			// delete the newly updated vehicle
			vehLocal.deleteVehicle(v2);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	private void testOrderStatelessEJB() {
		System.err.println("Calling testOrderStatelessEJB");
		try {
			Order order = new Order();
			order.setOrderTime(new Timestamp(new Date().getTime()));

			Book book = new Book();
			book.setIsbn("isbn #");
			book.setTitle("book Title");

			LineItem li = new LineItem();
			li.setPrice(10.00);
			li.setQuantity(10);
			li.setBook(book); // necessary
			li.setOrder(order); // necessary

			// Map<Integer, LineItem> lineItems = new HashMap<Integer,
			// LineItem>();
			// lineItems.put(1, li);
			List<LineItem> lineItems = new ArrayList<LineItem>();
			lineItems.add(li);
			order.setLineItem(lineItems);

			// create a new order and book
			orderSLLocal.createOrder(order, book);
			System.out.println("Order created with version: "
					+ order.getVersion());
			assert order.getVersion() == 1 : "Version error";

			orderSLLocal.createBookAndOrder(90.0);

			// retrieve all orders with order time before tomorrow
			long tomorrow = new Date().getTime() + 24 * 60 * 60 * 100;
			List<Order> orderList = orderSLLocal.retrieveOrders(new Timestamp(
					tomorrow));
			for (int i = 0; i < orderList.size(); i++) {
				System.out.println("retrieved order time: "
						+ orderList.get(i).getOrderTime());
			}

			// delete the newly inserted order and book
			orderSLLocal.deleteOrder(order, book);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	private void testOrderStatefulEJB() {
		System.err.println("Calling testOrderStatefulEJB");
		try {
			Order order = new Order();
			order.setOrderTime(new Timestamp(new Date().getTime()));

			Book book = new Book();
			book.setIsbn("isbn #");
			book.setTitle("book Title");

			LineItem li = new LineItem();
			li.setPrice(10.00);
			li.setQuantity(10);
			li.setBook(book); 
			li.setOrder(order); 

			List<LineItem> lineItems = new ArrayList<LineItem>();
			lineItems.add(li);
			order.setLineItem(lineItems);

			// create a new order and book
			orderSFLocal.init(order, book);
			orderSFLocal.createOrder();
			System.out.println("Order created with version: "
					+ order.getVersion());
			assert order.getVersion() >= 0 : "Version error";

			// retrieve all orders with order time before tomorrow
			long tomorrow = new Date().getTime() + 24 * 60 * 60 * 100;
			Timestamp tom = new Timestamp(tomorrow);
			List<Order> orderList = orderSFLocal.retrieveOrders(tom);
			for (int i = 0; i < orderList.size(); i++) {
				System.out.println("retrieved order time: "
						+ orderList.get(i).getOrderTime());
			}

			// delete the newly inserted order and book
			orderSFLocal.deleteOrder();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	private void testBookStatelessEJB() {
		System.err.println("Calling testBookStatelessEJB");
		try {
			Book book1 = new Book();
			book1.setIsbn("isbn1");
			book1.setTitle("Book1 Title");
			book1.setPrice(20.50);
			String content = "njenfuenmjvnjelenfe";
			byte[] pdf = content.getBytes();
			book1.setPdf(pdf);

			Category cat1 = new Category();
			cat1.setCategoryName("cat1 Social Literature");
			Set<Category> book1Cat = new HashSet<Category>();
			book1Cat.add(cat1);
			book1.setCategories(book1Cat);
			List<Book> books = new ArrayList<Book>();
			books.add(book1);
			cat1.setBooks(books);

			// create a new order and book
			Book b = bookSLLocal.createBook(book1, cat1);

			// find book by primary key
			Book bb = bookSLLocal.getBook(b.getBookId());
			System.out.println("retrieved book: " + bb.getTitle());
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	private void testBookStatelessEJB2() {
		System.err.println("Calling testBookStatelessEJB2");
		try {
			Book book1 = new Book();
			book1.setIsbn("isbn1");
			book1.setTitle("Book1 Title");
			book1.setPrice(20.50);
			String content = "njenfuenmjvnjelenfe";
			byte[] pdf = content.getBytes();
			book1.setPdf(pdf);

			Category cat1 = new Category();
			cat1.setCategoryName("cat1 Social Literature");
			Set<Category> book1Cat = new HashSet<Category>();
			book1Cat.add(cat1);
			book1.setCategories(book1Cat);
			List<Book> books = new ArrayList<Book>();

			books.add(book1);
			cat1.setBooks(books);

			// create a new order and book
			Book b = bookSL2Local.createBook(book1, cat1);

			// find book by primary key
			Book bb = bookSL2Local.getBook(b.getBookId());
			System.out.println("retrieved book: " + bb.getTitle());
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	private void testBookStatefulEJB() {
		System.err.println("Calling testBookStatefulEJB");
		try {
			Book book1 = new Book();
			book1.setIsbn("isbn1");
			book1.setTitle("Book1 Title");
			book1.setPrice(20.50);
			String content = "njenfuenmjvnjelenfe";
			byte[] pdf = content.getBytes();
			book1.setPdf(pdf);

			Category cat1 = new Category();
			cat1.setCategoryName("cat1 Social Literature");
			Set<Category> book1Cat = new HashSet<Category>();
			book1Cat.add(cat1);
			book1.setCategories(book1Cat);
			List<Book> books = new ArrayList<Book>();

			books.add(book1);
			cat1.setBooks(books);

			// create a new book and a category
			bookSFLocal.init(book1, cat1);
			Book b = bookSFLocal.createBook();

			// find book by primary key
			Book bb = bookSFLocal.getBook(b.getBookId());
			System.out.println("retrieved book: " + bb.getTitle());
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	private void testResources() throws NamingException, SQLException {
		InitialContext context = new InitialContext();
		UserTransaction ut = (UserTransaction) context
				.lookup("java:comp/UserTransaction");
		System.out.print("user transaction " + ut);

		DataSource ds = (DataSource) context.lookup("java:jpaTestDS");
		Connection cnn = ds.getConnection();
		System.out.print("connection from non-jta datasource: " + cnn);
		cnn.close();

		DataSource ds2 = (DataSource) context.lookup("java:jpaTestJtaDS");
		Connection cnn2 = ds2.getConnection();
		System.out.print("connection from non-jta datasource: " + cnn2);
		cnn2.close();
	}

	protected void testOrderEJB_EAOs() {

		Order order = new Order();
		order.setOrderTime(new Timestamp(new Date().getTime()));
		order.setPrice(1.5);

		LineItem li = new LineItem();
		li.setPrice(10.00);
		li.setQuantity(10);
		li.setBook(null);
		li.setOrder(order);

		List<LineItem> lineItems = new ArrayList<LineItem>();
		lineItems.add(li);
		order.setLineItem(lineItems);

		// test Order EAO with container-managed entity manager and JTA
		// transaction
		int loops = 10;
		long time = System.currentTimeMillis();
		for (int i = 0; i < loops; i++) {
			orderEaoLocal.createOrder(order);
			Order o = orderEaoLocal.queryOrderWithMaxId();
			orderEaoLocal.deleteOrder(order);
		}

		long time2 = System.currentTimeMillis();
		long totalTime = (time2 - time) / 100;
		System.out.println("EJB Order EAO's in seconds: " + totalTime);

		// test Order EAO with application-managed entity manager and non-JTA
		// transaction
		long time20 = System.currentTimeMillis();
		for (int i = 0; i < loops; i++) {
			orderEao2Local.createOrder(order);
			Order o2 = orderEaoLocal.queryOrderWithMaxId();
			orderEao2Local.deleteOrder(order);
		}

		long time22 = System.currentTimeMillis();
		long totalTime2 = (time22 - time20) / 100;
		System.out.println("EJB Order EAO2's in seconds: " + totalTime2);

	}

}
