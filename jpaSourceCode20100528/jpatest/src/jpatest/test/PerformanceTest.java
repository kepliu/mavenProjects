package jpatest.test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import jpatest.eao.BookCategoryEAO;
import jpatest.eao.CustomerEAO;
import jpatest.eao.OrderEAO;
import jpatest.entity.Address;

import jpatest.entity.BankInfo;
import jpatest.entity.Book;
import jpatest.entity.Category;
import jpatest.entity.Customer;
import jpatest.entity.CustomerType;
import jpatest.entity.GoldCustomer;
import jpatest.entity.LineItem;
import jpatest.entity.Order;
import jpatest.entity.OrderStatus;
import jpatest.entity.PreferredCustomer;

public class PerformanceTest {
	// There is one entity manager factory per persistence unit
	EntityManagerFactory emf = null;

	public PerformanceTest() {
		emf = Persistence.createEntityManagerFactory("jpaTestPU");
	}

	public void close() {
		if (emf != null && emf.isOpen()) {
			emf.close();
		}
	}
	
	public static void main(String[] args) {
		try {
			PerformanceTest perTest = new PerformanceTest();

			int totalCalls = 1;

			// do performance test and time it
			int totalOperations = 0;
			long time1 = System.currentTimeMillis();
			for (int i = 0; i < totalCalls; i++) {
				totalOperations += perTest.test();
			}
			long time2 = System.currentTimeMillis();

			long totalTime = (time2 - time1) / 1000;
			System.out.println("Total time: " + totalTime
					+ " seconds. Total operations: " + totalOperations
					+ ". Operations per second: " + totalOperations
					/ (totalTime + 0.0));

			perTest.close();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	/**
	 * Perform test and Return the number of operations (an operation is an insert, update,
	 * delete, or query)
	 */
	public int test() {
		// create a new entity manager instance from entity manager factory
		EntityManager em = emf.createEntityManager();

		int operations = 0;
		try {
			operations += createDeleteQueryBookCategory(em);
			operations += createDeleteQueryOrderLineItems(em);
			operations += createDeleteQueryCustomer(em);
		} catch (Throwable t) {
			t.printStackTrace();

		} finally {
			em.close(); // close entity manager
		}

		return operations;
	}

	/**
	 * Return the number of operations (one operation is an insert, update,
	 * delete, or query)
	 * 
	 * @param em
	 */
	protected int createDeleteQueryBookCategory(EntityManager em) {
		int operations = 0;

		Book book1 = new Book();
		book1.setIsbn("isbn1");
		book1.setTitle("Book1 Title");
		book1.setPrice(20.50);
		String content = "njenfuenmjvnjelenfe";
		byte[] pdf = content.getBytes();
		book1.setPdf(pdf);

		Book book2 = new Book();
		book2.setIsbn("isbn2");
		book2.setTitle("Book2 Title");
		book2.setPrice(30.50);

		Book book3 = new Book();
		book3.setIsbn("isbn3");
		book3.setTitle("Book3 Title");

		Category cat1 = new Category();
		cat1.setCategoryName("cat1 Social Literature");

		Category cat2 = new Category();
		cat2.setCategoryName("cat2 Novels");

		Category cat3 = new Category();
		cat3.setCategoryName("cat3 Fiction old");

		Category cat4 = new Category();
		cat4.setCategoryName("cat4 Fiction new");

		cat2.setParentCategory(cat1);
		cat3.setParentCategory(cat2);
		cat4.setParentCategory(cat2);

		Set<Category> book1Cat = new HashSet<Category>();
		book1Cat.add(cat1);
		book1Cat.add(cat2);
		book1Cat.add(cat3);
		book1.setCategories(book1Cat);

		Set<Category> book2Cat = new HashSet<Category>();
		book2Cat.add(cat1);
		book2Cat.add(cat2);
		book2Cat.add(cat4);
		book2.setCategories(book2Cat);

		Set<Category> book3Cat = new HashSet<Category>();
		book3Cat.add(cat1);
		book3.setCategories(book3Cat);

		List<Book> books = new ArrayList<Book>();
		books.add(book1);
		books.add(book2);
		books.add(book3);

		List<Category> categories = new ArrayList<Category>();
		categories.add(cat1);
		categories.add(cat2);
		categories.add(cat3);
		categories.add(cat4);

		// insert the books and categories into db
		BookCategoryEAO bcEao = new BookCategoryEAO();
		bcEao.createBookCategory(books, categories);
		operations += 7;  // 7 inserts (3 books, 4 categories) performed

		Category cc1 = em.find(Category.class, cat1.getCategoryId());
		operations++;     // one query performed

		List<Book> cat1Books = cc1.getBooks();

		Query query = em
				.createQuery("SELECT c FROM Category c WHERE c.categoryId = ?1");
		query.setParameter(1, cat1.getCategoryId());

		Category result = (Category) query.getSingleResult();
		operations++;     // one query performed

		List<Book> cat1Books2 = result.getBooks();

		Set<Category> cat1Children = result.getChildCategories();

		Category cc11 = em.find(Category.class, cat1.getCategoryId());
		operations++;     // one query performed

		List<Book> cat1Books3 = cc11.getBooks();

		Category c3 = em.find(Category.class, cat3.getCategoryId());
		operations++;     // one query performed

		List<Book> cat3Books = c3.getBooks();
		

		query.setParameter(1, cat3.getCategoryId());
		Category result3 = (Category) query.getSingleResult();
		operations++;     // one query performed

		List<Book> cat3Books2 = result3.getBooks();

		Category ca3 = em.find(Category.class, cat3.getCategoryId());
		operations++;     // one query performed

		List<Book> cat3Books3 = ca3.getBooks();

		// delete the books and categories

		List<Category> deleteCategories = new ArrayList<Category>();
		// due to parent-child relationship, delete in order
		// delete cat4 first, then cat3, then cat2, and finally delete cat1
		deleteCategories.add(0, categories.get(3));
		deleteCategories.add(1, categories.get(2));
		deleteCategories.add(2, categories.get(1));
		deleteCategories.add(3, categories.get(0));

		bcEao.deleteBookCategories(books, deleteCategories);
		operations += 7;    // 7 delete performed

		return operations;
	}

	/**
	 * Return the number of operations (one operation is an insert, update,
	 * delete, or query)
	 * 
	 * @param em
	 */
	protected int createDeleteQueryOrderLineItems(EntityManager em) {
		int operations = 0;

		Order order = new Order();
		order.setOrderTime(new Timestamp(new Date().getTime()));
		order.setPrice(100.00);
		order.setStatus(OrderStatus.BILLED);

		Customer customer = new Customer();
		customer.setName("John Customer");
		customer.setPicture("picture".getBytes());
		customer.setCustomerType(CustomerType.C);
		order.setCustomer(customer);
		
		BankInfo bi = new BankInfo();
		bi.setBankName("NationalBank");
		bi.setRoutingNumber("1234567");
		bi.setAccountNumber("99887766-2");
		customer.setBank(bi);

		LineItem li = new LineItem();
		li.setPrice(10.00);
		li.setQuantity(10);
		li.setOrder(order);

		Book book = new Book();
		book.setIsbn("isbn #");
		book.setTitle("book Title");
		li.setBook(book);

		LineItem li2 = new LineItem();
		li2.setPrice(10.00);
		li2.setQuantity(10);
		li2.setBook(book); 
		li2.setOrder(order); 

		List<LineItem> lineItems = new ArrayList<LineItem>();
		lineItems.add(li);
		lineItems.add(li2);
		order.setLineItem(lineItems);

		CustomerEAO custEao = new CustomerEAO();
		custEao.createCustomer(new Customer[] { customer });
		operations++;

		BookCategoryEAO bookEao = new BookCategoryEAO();
		List<Book> books = new ArrayList<Book>();
		books.add(book);
		bookEao.createBookCategory(books, new ArrayList<Category>());
		operations++;

		OrderEAO orderEao = new OrderEAO();
		orderEao.createOrder(order);
		operations += 1;

		
		// Hibernate fails when deleting the associated line items.
		orderEao.deleteOrder(order);
		operations += 1;

		custEao.deleteCustomer(new Customer[] { customer });
		operations++;

		BookCategoryEAO bcEao = new BookCategoryEAO();
		List<Category> categories = new ArrayList<Category>();
		bcEao.deleteBookCategories(books, categories);
		operations++;

		return operations;
	}

	/**
	 * Return the number of operations (one operation is an insert, update,
	 * delete, or query)
	 * 
	 * @param em
	 */
	protected int createDeleteQueryCustomer(EntityManager em) {
		int operations = 0;

		CustomerEAO cEao = new CustomerEAO();

		Customer c = new Customer();
		c.setName("John Customer");
		c.setPicture("picture".getBytes());
		c.setCustomerType(CustomerType.C);

		Address ad = new Address();
		ad.setStreet("2920 Main Street");
		ad.setCity("LocalTown");
		ad.setState("MI");
		ad.setZip("18081");
		c.setAddress(ad);

		BankInfo bi = new BankInfo();
		bi.setBankName("NationalBank");
		bi.setRoutingNumber("1234567");
		bi.setAccountNumber("99887766-2");
		c.setBank(bi);

		PreferredCustomer p = new PreferredCustomer();
		p.setName("John Preferred");
		p.setPicture("picture".getBytes());
		p.setCustomerType(CustomerType.P);
		p.setDiscountRate(0.20);
		p.setExpirationDate(new Date());

		Address adp = new Address();
		adp.setStreet("2920 Peach Street");
		adp.setCity("Peach Town");
		adp.setState("MI");
		adp.setZip("18082");
		p.setAddress(adp);

		BankInfo bip = new BankInfo();
		bip.setBankName("National Peach Bank");
		bip.setRoutingNumber("1234568");
		bip.setAccountNumber("99887766-3");
		p.setBank(bip);

		GoldCustomer g = new GoldCustomer();
		g.setName("John Gold");
		g.setPicture("picture".getBytes());
		g.setCustomerType(CustomerType.G);
		g.setCardNumber("myCard#");

		Address adg = new Address();
		adg.setStreet("2920 Gold Street");
		adg.setCity("Gold Town");
		adg.setState("MI");
		adg.setZip("18083");
		g.setAddress(adg);

		BankInfo big = new BankInfo();
		big.setBankName("National Gold Bank");
		big.setRoutingNumber("1234568");
		big.setAccountNumber("99887766-4");
		g.setBank(big);

		// create new customer, preferred customer, and gold customer
		Customer[] cs = new Customer[] { c, p, g };
		cEao.createCustomer(cs);
		operations += 6; // created 3 customers adn 3 addresses

		c.setName("John Customer II");
		p.setName("John Preferred II");
		p.setDiscountRate(0.40);
		g.setName("John Gold  II");

		BankInfo bip2 = new BankInfo();
		bip2.setBankName("National 2nd Peach Bank");
		bip2.setRoutingNumber("1234569");
		bip2.setAccountNumber("99887766-33");
		p.setBank(bip2);

		// update customers with the new values
		Customer[] customers = cEao.updateCustomer(cs);
		operations += 3; // updated 3 customers

		// delete these objects from database
		cEao.deleteCustomer(customers);
		operations += 6; // delete 3 customers and 3 addresses

		em = cEao.createEntityManager();
		Customer c0 = em.find(Customer.class, customers[0].getCustomerId());
		operations++;

		return operations;
	}

}
