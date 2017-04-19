package jpatest.test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Parameter;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;


import org.junit.Test;

import jpatest.eao.BookCategoryEAO;
import jpatest.eao.CustomerEAO;
import jpatest.eao.OrderEAO;
import jpatest.entity.Address;
import jpatest.entity.Address2;
import jpatest.entity.Address3;
import jpatest.entity.BankInfo;
import jpatest.entity.Book;
import jpatest.entity.Category;
import jpatest.entity.Customer;
import jpatest.entity.Customer5;
import jpatest.entity.CustomerType;
import jpatest.entity.GoldCustomer;
import jpatest.entity.GoldCustomer5;
import jpatest.entity.LineItem;
import jpatest.entity.Order;
import jpatest.entity.OrderDetail;
import jpatest.entity.OrderStatus;
import jpatest.entity.PreferredCustomer;
import jpatest.entity.PreferredCustomer5;

/**
 * This class tests the queries for the JPQL chapter
 * 
 */
public class QueryTest extends MyJpaTestCase {
	@Test
	public void testQueries() {
		try {
			createEntities(em);

			// test queries
			runNamedQueries();
			runQueryReturnTypes();
			runTypedQueries();
			runFromClauseExamples();
			runConditionalExpressions();
			runScalerFunctions();
			runCaseExpressions();
			runOrderByQuery();
			runGroupByQuery();
			runEmbeddable();
			runAggregateFunctions();
			runPolymorphicQuery();
			runNativeQueries();
			runAppExample();
			
			runQueryPerformance();

			// must delete at the end
			runBulkOperations(em);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	protected void runNamedQueries() {
		System.out.flush();
		System.out.println("runnig testNamedQueries(): ");

		// named query
		{
			List<Object[]> resultList = em.createNamedQuery(
					"selectCustomerAndPrice2").setParameter(1, new Date(),
					TemporalType.TIMESTAMP).getResultList();
			for (Object[] result : resultList) {
				Customer c = (Customer) result[0];
				Double p = (Double) result[1];
				String name = null;
				if (c != null)
					name = c.getName();
				System.out.println("named query, customer: " + name
						+ " price : " + p);
			}
		}

		{
			List<Object[]> resultList = em.createNamedQuery(
					"selectCustomerAndPrice3").setParameter(1, new Date(),
					TemporalType.TIMESTAMP).getResultList();
			for (Object[] result : resultList) {
				Customer c = (Customer) result[0];
				Double p = (Double) result[1];
				String name = null;
				if (c != null)
					name = c.getName();
				System.out.println("named query, customer: " + name
						+ " price : " + p);
			}
		}

		{
			List<Double> resultList = em.createNamedQuery("selectOrderPrice")
					.setParameter("status", OrderStatus.BILLED).getResultList();

			for (Double result : resultList) {
				System.out.println("named query, order price: " + result);
			}
		}
	}

	protected void runQueryReturnTypes() {
		System.out.flush();
		System.out.println("\nrunnig testQueryReturnTypes(): ");

		// return types for queries
		{
			// single select expression -> return row type: Object
			// Return an entity single result which is the order with max
			// orderId
			Query query = em
					.createQuery("SELECT o FROM Order o "
							+ "WHERE o.orderId = (SELECT MAX(o2.orderId) FROM Order o2)");
			Order order = (Order) query.getSingleResult();

			System.out.println("order with max orderId : " + order.getPrice());
		}

		{
			// single select expression -> return row type: Object
			// Return a scalar single result, which is the average price of
			// billed
			// orders
			// The JPQL average function AVG is used, which always returns
			// Double
			Query query = em
					.createQuery("SELECT AVG(o.price) FROM Order o WHERE o.status = :status");
			query.setParameter("status", OrderStatus.BILLED);
			Double price = (Double) query.getSingleResult();

			System.out.println("order average price : " + price);
		}

		{
			// A query with multiple select expressions. It returns a single
			// result
			// of type: Object[], which is a combination of entity and scalar
			Query query = em
					.createQuery("SELECT o, o.customer.bank.bankName FROM Order o "
							+ " WHERE o.orderId = (SELECT MAX(o2.orderId) FROM Order o2)");
//			Object[] result = (Object[]) query.getSingleResult();
//			Order order = (Order) result[0];
//			String bank = (String) result[1];
//
//			System.out.println("order with max orderId : price:"
//					+ order.getPrice());
//			System.out.println("order with max orderId : bank:" + bank);
		}

		// return a list of entity
		{
			// single select expression -> return row type: Object
			// Single select expression. Returns a list of Customers who ordered
			// before today
			Query query = em
					.createQuery("SELECT o.customer FROM Order o WHERE o.orderTime < ?1");
			query.setParameter(1, new Date(), TemporalType.TIMESTAMP);
			List<Customer> resultList = query.getResultList();

			for (Object result : resultList) {
				Customer customer = (Customer) result;
				if (customer != null)
				System.out.println("Customer name: " + customer.getName());
			}
		}

		// return a list of scalar
		{
			// single select expression -> return row type: Object
			// Single select expression. Returns a list of prices with billed
			// order
			// status
			Query query = em
					.createQuery("SELECT o.price FROM Order o WHERE o.status = :status");
			query.setParameter("status", OrderStatus.BILLED);
			List<Double> resultList = query.getResultList();

			for (Double result : resultList) {
				System.out.println("order price: " + result);
			}
		}

		// return a list of entity and scalar
		{
			// multiple select expression -> return row type: Object[]
			// Multiple select expression. Return a list of Object[]
			Query query = em
					.createQuery("SELECT o.customer, o.price FROM Order o WHERE o.orderTime < ?1");
			query.setParameter(1, new Date(), TemporalType.TIMESTAMP);
			query.setMaxResults(1);
			query.setFirstResult(0);
			List<Object[]> resultList = query.getResultList();

			// process the first screen of records
			for (Object[] result : resultList) {
				Customer customer = (Customer) result[0];
				Double price = (Double) result[1];
				String name = null;
				if (customer != null) name = customer.getName();

				System.out.println("customer first batch : "
						+ name + " price : " + price);
			}

			// retrieve the second screen of records
			query.setMaxResults(2);
			query.setFirstResult(1);
			resultList = query.getResultList();
			for (Object[] result : resultList) {
				Customer customer = (Customer) result[0];
				Double price = (Double) result[1];
                String name = null;
                if (customer != null) name = customer.getName();
				System.out.println("customer 2nd batch: " + name
						+ " price : " + price);
			}

		}

		// constructor query
		{
			Query query = em
					.createQuery("SELECT NEW jpatest.entity.OrderDetail(o.customer.name, o.price) FROM Order o WHERE o.orderTime < ?1");
			query.setParameter(1, new Date(), TemporalType.TIMESTAMP);
			List<OrderDetail> resultList = query.getResultList();
			for (OrderDetail result : resultList) {
				String oname = result.customer;
				double p = result.price;
				System.out.println("order detail: customer name: " + oname
						+ " price : " + p);
			}
		}
	}

	protected static void runBulkOperations(EntityManager em) {
		System.out.flush();
		System.out.println("\nrunnig testBulkOperations(): ");

		// test bulk updates
		{
			em.clear();
			em.getTransaction().begin();
			Query query = em
					.createQuery("UPDATE Order o SET o.status = jpatest.entity.OrderStatus.BILLED, o.price = 10, o.version = o.version + 1"
							+ "WHERE o.customer.name LIKE ?1");
			query.setParameter(1, "John%");
			int updated = query.executeUpdate();
			em.getTransaction().commit();
			System.out.println("updated : " + updated);
		}

		{
			
			em.getTransaction().begin();
			Query q = em.createQuery("DELETE FROM LineItem");
			int deleted = q.executeUpdate();
			em.getTransaction().commit();
			System.out.println("bulk deleted LineItem: " + deleted);
		}

		{
			em.getTransaction().begin();
			Query q = em
					.createQuery("DELETE FROM Order o WHERE o.customer.name LIKE 'John%'");
			int deleted = q.executeUpdate();
			em.getTransaction().commit();
			System.out.println("bulk deleted Order: " + deleted);
		}

		{
			em.getTransaction().begin();
			Query q = em.createQuery("DELETE FROM Order o");
			int deleted = q.executeUpdate();
			em.getTransaction().commit();
			System.out.println("bulk deleted Order: " + deleted);
		}

		{
			em.getTransaction().begin();
			Query q = em.createQuery("DELETE FROM PreferredCustomer5");
			int deleted = q.executeUpdate();
			em.getTransaction().commit();
			System.out.println("bulk deleted PreferredCustomer5: " + deleted);
		}

		{
			em.getTransaction().begin();
			Query q = em.createQuery("DELETE FROM GoldCustomer5");
			int deleted = q.executeUpdate();
			em.getTransaction().commit();
			System.out.println("bulk deleted GoldCustomer5: " + deleted);
		}

		{
			em.getTransaction().begin();
			Query q = em.createQuery("DELETE FROM Customer5");
			int deleted = q.executeUpdate();
			em.getTransaction().commit();
			System.out.println("bulk deleted Customer5: " + deleted);
		}

		{
			em.getTransaction().begin();
			Query q = em.createQuery("DELETE FROM PreferredCustomer");
			int deleted = q.executeUpdate();
			em.getTransaction().commit();
			System.out.println("bulk deleted PreferredCustomer: " + deleted);
		}

		{
			em.getTransaction().begin();
			Query q = em.createQuery("DELETE FROM GoldCustomer");
			int deleted = q.executeUpdate();
			em.getTransaction().commit();
			System.out.println("bulk deleted GoldCustomer: " + deleted);
		}

		{
			em.getTransaction().begin();
			Query q = em.createQuery("DELETE FROM Customer");
			int deleted = q.executeUpdate();
			em.getTransaction().commit();
			System.out.println("bulk deleted Customer: " + deleted);
		}
	
		{
			em.getTransaction().begin();
			Query q = em.createQuery("DELETE FROM PreferredCustomer2");
			int deleted = q.executeUpdate();
			em.getTransaction().commit();
			System.out.println("bulk deleted PreferredCustomer2: " + deleted);
		}
		{
			em.getTransaction().begin();
			Query q = em.createQuery("DELETE FROM GoldCustomer2");
			int deleted = q.executeUpdate();
			em.getTransaction().commit();
			System.out.println("bulk deleted GoldCustomer2: " + deleted);
		}
		
		{
			em.getTransaction().begin();
			Query q = em.createQuery("DELETE FROM Customer2");
			int deleted = q.executeUpdate();
			em.getTransaction().commit();
			System.out.println("bulk deleted Customer2: " + deleted);
		}
		
		{
			em.getTransaction().begin();
			Query q = em.createQuery("DELETE FROM PreferredCustomer3");
			int deleted = q.executeUpdate();
			em.getTransaction().commit();
			System.out.println("bulk deleted PreferredCustomer3: " + deleted);
		}
		{
			em.getTransaction().begin();
			Query q = em.createQuery("DELETE FROM GoldCustomer3");
			int deleted = q.executeUpdate();
			em.getTransaction().commit();
			System.out.println("bulk deleted GoldCustomer3: " + deleted);
		}
		
		{
			em.getTransaction().begin();
			Query q = em.createQuery("DELETE FROM Customer3");
			int deleted = q.executeUpdate();
			em.getTransaction().commit();
			System.out.println("bulk deleted Customer3: " + deleted);
		}

		{
			em.getTransaction().begin();
			Query q = em.createQuery("DELETE FROM Address2");
			int deleted = q.executeUpdate();
			em.getTransaction().commit();
			System.out.println("bulk deleted Address2: " + deleted);
		}
		{
			em.getTransaction().begin();
			Query q = em.createQuery("DELETE FROM Address");
			int deleted = q.executeUpdate();
			em.getTransaction().commit();
			System.out.println("bulk deleted Address: " + deleted);
		}
	}

	protected  void runFromClauseExamples() {
		System.out.flush();
		System.out.println("\nrunnig runFromClauseExamples(): ");

		// test JPQL exmaples
		{
			Query query = em
					.createQuery("SELECT DISTINCT o FROM Order AS o JOIN o.lineItems AS li WHERE li.price > 50");
			query.setMaxResults(50);
			query.setFirstResult(0);
			List<Order> resultList = query.getResultList();
			for (Order result : resultList) {
				System.out.println("order with price >50 : "
						+ result.getOrderId());
			}
		}

		{
			Query query = em
					.createQuery("SELECT DISTINCT o FROM Order o, Order o2 WHERE o.price > o2.price AND o2.customer.name = 'John Customer'");
			query.setMaxResults(50);
			query.setFirstResult(0);
			List<Order> resultList = query.getResultList();
			for (Order result : resultList) {
				System.out.println("order with price > John Wang' : "
						+ result.getOrderId());
			}
		}
		{
			Query query = em
					.createQuery("SELECT i.book FROM Order o, IN(o.lineItems) i WHERE i.quantity >= 1");
			query.setMaxResults(50);
			query.setFirstResult(0);
			List<Book> resultList = query.getResultList();
			for (Book result : resultList) {
				String title = null;
				if (result != null) title = result.getTitle();
				System.out.println("book with quantity > 5' : " + title);
			}

		}

		{
			{ // map collection
				TypedQuery<Order> q = em
						.createQuery(
								"SELECT VALUE(o) FROM Customer AS c JOIN c.orders AS o WHERE KEY(o) > 22",
								Order.class);
				// q.setParameter(1, new Integer(5)); // how to pass this
				// parameter?
				List<Order> resultList = q.getResultList();
				for (Order o : resultList) {
					System.out.println("Map collection order: "
							+ o.getOrderId());
				}
			}
		}

		{
			Query query = em
					.createQuery("SELECT i.book FROM Order o JOIN o.lineItems i WHERE i.quantity >= 1");
			query.setMaxResults(50);
			query.setFirstResult(0);
			List<Book> resultList = query.getResultList();
			for (Book result : resultList) {
				String title = null;
				if (result != null) title = result.getTitle();
				System.out.println("book with quantity > 5' : " + title);
			}
		}

		{
			Query query = em
					.createQuery("SELECT DISTINCT o FROM Order o JOIN o.lineItems l JOIN l.book b WHERE b.title LIKE '%Java%'");
			query.setMaxResults(50);
			query.setFirstResult(0);
			List<Order> resultList = query.getResultList();
			for (Order result : resultList) {
				System.out.println("order with inner join ' : "
						+ result.getOrderId());
			}
		}

		{
			Query query = em
					.createQuery("SELECT a, b FROM Address a, Book b WHERE a.addressId = b.bookId");
			query.setMaxResults(50);
			query.setFirstResult(0);
			List<Object[]> resultList = query.getResultList();
			for (Object[] result : resultList) {
				Address address = (Address) result[0];
				Book book = (Book) result[1];
				System.out.println("Book with theta join address' : "
						+ address.getCity());
				System.out.println("Book with theta join book' : "
						+ book.getTitle());
			}
		}

		{
			Query query = em
					.createQuery("SELECT b, c FROM Book b LEFT OUTER JOIN b.categories c WHERE b.title LIKE '%Java%'");
			query.setMaxResults(50);
			query.setFirstResult(0);
			List<Object[]> resultList = query.getResultList();
			for (Object[] result : resultList) {
				Book book = (Book) result[0];
				Category cat = (Category) result[1];
				System.out.println("Book with outer join category book : "
						+ book.getTitle());
				System.out.println("Book with outer join category: cat : "
						+ cat);
			}
		}

		{
			Query query = em
					.createQuery("SELECT c, o FROM Customer c LEFT JOIN c.orders o WHERE c.bank.bankName LIKE ?1");
			query.setMaxResults(50);
			query.setFirstResult(0);
			query.setParameter(1, "National Peach Bank");
			List<Object[]> resultList = query.getResultList();
			for (Object[] result : resultList) {
				Customer customer = (Customer) result[0];
				Order order = (Order) result[1];
				System.out.println("customer with outer join order: "
						+ customer.getName());
				System.out.println("customer with outer join order: " + order);
			}
		}

		{
			Query query = em
					.createQuery("SELECT c FROM Customer c LEFT JOIN FETCH c.orders WHERE c.customerType = ?1");
			query.setParameter(1, CustomerType.G);
			List<Customer> resultList = query.getResultList();
			for (Customer result : resultList) {
				System.out.println("customer with left fetch join order: "
						+ result.getName());
			}
		}
	}

	protected void runPolymorphicQuery() {
		System.out.flush();
		System.out.println("\nrunnig testPolymorphicQuery(): ");

		{
			Query query = em
					.createQuery("SELECT c FROM Customer c WHERE c.name LIKE ?1 AND c.income > ?2");
			query.setMaxResults(50);
			query.setFirstResult(0);
			query.setParameter(1, "J%");
			query.setParameter(2, 50);
			List<Customer> objList = query.getResultList();

			for (Customer c : objList) {
				if (c instanceof PreferredCustomer) {
					System.out.println("PreferredCustomer : " + c.getName());
				} else if (c instanceof GoldCustomer) {
					System.out.println("GolderCustomer : " + c.getName());
				} else {
					System.out.println("Customer : " + c.getName());
				}
			}
		}

		{
			Query query = em
					.createQuery("SELECT AVG(c.income) FROM Customer c WHERE c.name LIKE 'J%' AND c.picture IS NOT NULL");
			Double avgIncome = (Double) query.getSingleResult();
			System.out.println("average income for Customer : " + avgIncome);
		}
		
		{
			Query query = em
					.createQuery("SELECT AVG(c.income) FROM Customer c WHERE TYPE(c) = GoldCustomer");
			Double avgIncome = (Double) query.getSingleResult();
			System.out.println("average income for gold Customer : " + avgIncome);
		}
	}

	protected  void runOrderByQuery() {
		System.out.flush();
		System.out.println("\nrunnig testOrderByQuery(): ");

		{
			Query query = em
					.createQuery("SELECT o.price, a.zip FROM Customer c JOIN c.orders o JOIN c.address a WHERE a.state = 'MI' AND a.city = 'Troy' ORDER BY a.zip ASC, o.price DESC");
			List<Object[]> objList = query.getResultList();

			System.out.println("order by query returned " + objList.size()
					+ " results");
			for (Object[] result : objList) {
				Double price = (Double) result[0];
				String zip = (String) result[1];
				System.out
						.println("order by price : " + price + " zip: " + zip);
			}
		}

		{
			// Need JPA 2.0. Tested
			Query query = em
					.createQuery("SELECT SUM(o.price) AS p, a.zip FROM Customer c JOIN c.orders o JOIN c.address a GROUP BY a.zip ORDER BY p DESC");
			List<Object[]> objList = query.getResultList();
			for (Object[] result : objList) {
				Double price = (Double) result[0];
				String zip = (String) result[1];
				System.out.println("order by sum of price : " + price
						+ " zip: " + zip);
			}
		}
	}

	protected void runGroupByQuery() {
		System.out.flush();
		System.out.println("\nrunnig testGroupByQuery(): ");

		{
			Query query = em
					.createQuery("SELECT o.status, AVG(o.price), COUNT(o) FROM Order o GROUP BY o.status HAVING o.status IN (?1, ?2)");
			query.setParameter(1, OrderStatus.BILLED);
			query.setParameter(2, OrderStatus.NEW);
			List<Object[]> objList = query.getResultList();

			System.out.println("group by query returned " + objList.size()
					+ " results");
			for (Object[] result : objList) {
				OrderStatus status = (OrderStatus) result[0];
				Double price = (Double) result[1];
				Long count = (Long) result[2];
				System.out.println("group by status : " + status + " price: "
						+ price + " count: " + count);
			}
		}

		{
			Query query = em
					.createQuery("SELECT c.bank.bankName, COUNT(c) FROM Customer c GROUP BY c.bank.bankName HAVING COUNT(c.bank.bankName) > 30");
			List<Object[]> objList = query.getResultList();
			for (Object[] result : objList) {
				String name = (String) result[0];
				Long count = (Long) result[1];
				System.out.println("order by bank name : " + name + " count: "
						+ count);
			}
		}
	}
	
	protected  void runEmbeddable() {
		System.out.flush();
		System.out.println("\nrunnig runEmbeddable(): ");
		{
			Query query = em
					.createQuery("SELECT DISTINCT c.bank FROM Customer c WHERE SIZE(c.orders) >= 1");
			List<BankInfo> bis = query.getResultList();
			for (BankInfo bi: bis){
				System.out.println("bank info: " + bi.getBankName());
			}
		}
		
	}

	protected  void runAggregateFunctions() {
		System.out.flush();
		System.out.println("\nrunnig testAggregateFunctions(): ");
		{
			Query query = em
					.createQuery("SELECT AVG(o.price) FROM Order o WHERE o.status = ?1");
			query.setParameter(1, OrderStatus.BILLED);
			Double price = (Double) query.getSingleResult();

			System.out.println("aggregate function avg price " + price);
		}

		{
			Query query = em
					.createQuery("SELECT SUM(li.price)FROM Order o JOIN o.lineItems li JOIN o.customer c WHERE c.name LIKE ?1 AND o.status = ?2");
			query.setParameter(1, "J%");
			query.setParameter(2, OrderStatus.BILLED);
			Double price = (Double) query.getSingleResult();

			System.out.println("aggregate function sum price " + price);
		}

		{
			Query query = em
					.createQuery("SELECT COUNT(o) FROM Order o JOIN o.customer c WHERE c.name LIKE 'John%'");

			Long count = (Long) query.getSingleResult();

			System.out.println("aggregate function count " + count);
		}
	}

	protected  void runCaseExpressions() {
		System.out.flush();
		System.out.println("\nrunnig testCaseExpressions(): ");	

		{ // CASE function: This one works. But doing UPDATE will cause
			// problem when deleting. So comment it out.
			Query q = em
					.createQuery("UPDATE Book b SET b.price = CASE WHEN b.rating = 1 THEN b.price * 1.2 WHEN b.rating = 2 THEN b.price * 1.1 ELSE b.price * 1.03 END");
			// em.getTransaction().begin();
			// int updated = q.executeUpdate();
			// em.getTransaction().commit();

			// System.out.println("newJPQLqueries CASE book: " + updated);

		}

		{ // CASE function: Not Working
			Query q = em
					.createQuery("UPDATE Book b SET b.price = CASE b.rating WHEN 1 THEN b.price * 1.2 WHEN 2 THEN b.price * 1.1 ELSE b.price * 1.03 END");
			// em.getTransaction().begin();
			// int updated = q.executeUpdate();
			// em.getTransaction().commit();
			// System.out.println("newJPQLqueries CASE 2 book: " + updated);
		}

		{ // CASE function:
			Query q = em
					.createQuery("SELECT c.name, CASE WHEN TYPE(c) = PreferredCustomer THEN 'Preferred Customer' WHEN TYPE(c) = GoldCustomer THEN 'Gold Customer' ELSE 'Normal Customer' END FROM Customer c WHERE c.name LIKE 'John%'");

			List<Object[]> resultList = q.getResultList();
			for (Object[] o : resultList) {
				String name = (String) o[0];
				String cust = (String) o[1];
				System.out.println("newJPQLqueries CASE Type customer: " + name
						+ ":" + cust);
			}
		}
		
		{
			Query q = em
					.createQuery("SELECT o.customer.name, i.book, CASE WHEN i.quantity > 10 THEN 'Large ' WHEN i.quantity > 5  THEN 'Medium ' ELSE 'Small or No ' END FROM Order o JOIN o.lineItems i");
			List resultList = q.getResultList();
			for (Object o : resultList) {
				Object[] result = (Object[]) o;
				String name = (String) result[0];
				Book book = (Book) result[1];
				String size = (String) result[2];
				System.out.println("newJPQLqueries case: " + name + ":"
						+ book.getIsbn() + ":" + size);
			}
		}

	}

	protected  void runScalerFunctions() {
		System.out.flush();
		System.out.println("\nrunnig testScalerFunctions(): ");

		// string functions
		{
			Query query = em
					.createQuery("SELECT book FROM Book book WHERE CONCAT (book.title, 's') = 'Java by Examples'");

			List<Book> objList = query.getResultList();
			for (Book result : objList) {
				System.out.println("scalar function book : "
						+ result.getTitle());
			}
		}

		{
			Query query = em
					.createQuery("SELECT book FROM Book book WHERE SUBSTRING (book.title, 1, 4) = 'Java'");

			List<Book> objList = query.getResultList();
			for (Book result : objList) {
				System.out.println("scalar function book : "
						+ result.getTitle());
			}
		}

		{
			Query query = em
					.createQuery("SELECT book FROM Book book WHERE TRIM (LEADING FROM book.title) = 'Java  Persistence with JPA'");

			List<Book> objList = query.getResultList();
			for (Book result : objList) {
				System.out.println("scalar function book : "
						+ result.getTitle());
			}
		}

		{
			Query query = em
					.createQuery("SELECT b FROM Book b WHERE LOCATE ('Java', b.title) = 1");

			List<Book> objList = query.getResultList();
			for (Book result : objList) {
				System.out.println("scalar function book : "
						+ result.getTitle());
			}
		}

		// arithmetic functions
		{
			Query query = em
					.createQuery("SELECT li FROM LineItem li WHERE SQRT(li.price)  >= 4.0");
			List<LineItem> objList = query.getResultList();
			for (LineItem result : objList) {
				System.out.println("scalar function sqrt(price) : "
						+ result.getPrice());
			}
		}

		{
			Query query = em
					.createQuery("SELECT li FROM Order o JOIN o.lineItems li WHERE MOD(li.quantity, 2) = 1");
			List<LineItem> objList = query.getResultList();
			for (LineItem result : objList) {
				System.out.println("scalar function MOD(li.quantity, 2) : "
						+ result.getQuantity());
			}
		}

		{
			Query query = em
					.createQuery("SELECT o FROM Order o WHERE SIZE(o.lineItems) >= 1");
			List<Order> objList = query.getResultList();
			for (Order result : objList) {
				System.out.println("scalar function SIZE(o.lineItems ) : "
						+ result.getLineItems().size());
			}
		}

		{ // index function
			TypedQuery<LineItem> q = em
					.createQuery(
							"SELECT i FROM Order o JOIN o.lineItems i WHERE o.price > 2 AND INDEX(i) BETWEEN 0 AND ?1",
							LineItem.class);
			q.setParameter(1, 1000);
			List<LineItem> resultList = q.getResultList();
			for (LineItem o : resultList) {
				System.out.println("newJPQLqueries index LineItem: "
						+ o.getLineItemId());
			}
		}

		// date functions
		{
			Query query = em
					.createQuery("SELECT pc FROM PreferredCustomer pc  WHERE pc.expirationDate = CURRENT_DATE");
			List<PreferredCustomer> objList = query.getResultList();
			for (PreferredCustomer result : objList) {
				System.out.println("scalar function current_date : "
						+ result.getExpirationDate());
			}
		}

		{ // type function
			TypedQuery<Customer> q = em
					.createQuery(
							"SELECT c FROM Customer c WHERE TYPE(c) IN (PreferredCustomer, GoldCustomer)",
							Customer.class);
			List<Customer> resultList = q.getResultList();
			for (Customer o : resultList) {
				System.out.println(" type customer: " + o.getCustomerType());
			}
		}

		{ // type function
			TypedQuery<Customer> q = em
					.createQuery(
							"SELECT c FROM Customer c WHERE TYPE(c) IN (:customerType1,:customerType2)",
							Customer.class);
			q.setParameter("customerType1", PreferredCustomer.class);
			q.setParameter("customerType2", GoldCustomer.class);

			List<Customer> resultList = q.getResultList();
			for (Customer o : resultList) {
				System.out.println(" type  customer: " + o.getCustomerType());
			}
		}

		{ // type function
			TypedQuery<Customer> q = em.createQuery(
					"SELECT c FROM Customer c WHERE TYPE(c) <> GoldCustomer",
					Customer.class);
			List<Customer> resultList = q.getResultList();
			for (Customer o : resultList) {
				System.out.println(" type not gold customer: "
						+ o.getCustomerType());
			}
		}
	}

	protected  void runConditionalExpressions() {
		System.out.flush();
		System.out.println("\nrunnig testConditionalExpressions(): ");
		
		{ // temporal literals
			Query query = em
					.createQuery("SELECT p FROM PreferredCustomer p WHERE p.expirationDate < {d '2010-12-31'} ");

			List<PreferredCustomer> resultList = query.getResultList();
			for (PreferredCustomer result : resultList) {
				System.out
						.println("testConditionalExpressions temportal literal: "
								+ result.getExpirationDate());
			}
		}
		{ // temporal literals
			Query query = em
					.createQuery("SELECT o FROM Order o WHERE o.orderTime < {ts '2010-12-31 11:45:10.513'} ");

			List<Order> resultList = query.getResultList();
			for (Order result : resultList) {
				System.out
						.println("testConditionalExpressions temportal literal: "
								+ result.getOrderTime());
			}
		}

		// operators
		{
			Query query = em
			// .createQuery("SELECT li FROM Order o JOIN o.lineItems li WHERE o.price *0.8 > li.price");
					.createQuery("SELECT li FROM Order o JOIN o.lineItems li WHERE o.price BETWEEN 10 AND 300");

			List<LineItem> resultList = query.getResultList();
			for (LineItem result : resultList) {
				System.out
						.println("testConditionalExpressions operator price: "
								+ result.getPrice());
			}
		}

		{ // type function
			TypedQuery<Customer> q = em
					.createQuery(
							"SELECT c FROM Customer c WHERE TYPE(c) IN (PreferredCustomer, GoldCustomer)",
							Customer.class);

			List<Customer> resultList = q.getResultList();
			for (Customer o : resultList) {

				System.out.println("newJPQLqueries type  customer: "
						+ o.getCustomerType());

			}
		}

		{
			Query query = em
					.createQuery("SELECT c FROM Customer c  WHERE c.name IN('John Gold', 'John Customer', 'John Preffered')");

			List<Customer> resultList = query.getResultList();
			for (Customer result : resultList) {
				System.out
						.println("testConditionalExpressions operator customer: "
								+ result.getName());
			}
		}

		{
			Query query = em
					.createQuery("SELECT DISTINCT o FROM Order o JOIN o.lineItems l WHERE l.book.title IN (SELECT b.title FROM Book b JOIN b.categories c WHERE c.categoryName ='Fiction')");
			List<Order> resultList = query.getResultList();
			for (Order result : resultList) {
				System.out
						.println("testConditionalExpressions subquery order: "
								+ result.getOrderId());
			}
		}

		{ // New feature of JPA 2.0 using an array in IN
			Query q = em
					.createQuery("SELECT o FROM Order o WHERE o.orderId IN ?1");
			List<Integer> orderIds = new ArrayList<Integer>();
			orderIds.add(102);
			orderIds.add(152);
			q.setParameter(1, orderIds);
			List<Order> resultList = q.getResultList();
			for (Order o : resultList) {
				System.out
						.println("newJPQLqueries IN order: " + o.getOrderId());
			}
		}

		{ // type and IN function
			TypedQuery<Customer> q = em.createQuery(
					"SELECT c FROM Customer c WHERE TYPE(c) IN :customerTypes",
					Customer.class);
			List<Class<? extends Customer>> cc = new ArrayList<Class<? extends Customer>>();
			cc.add(PreferredCustomer.class);
			cc.add(GoldCustomer.class);

			q.setParameter("customerTypes", cc);

			List<Customer> resultList = q.getResultList();
			for (Customer o : resultList) {
				System.out.println("newJPQLqueries type & IN  customer: "
						+ o.getCustomerType());

			}
		}

		{
			Query query = em
					.createQuery("SELECT  b FROM Book b WHERE b.title IS NOT NULL");
			List<Book> resultList = query.getResultList();
			for (Book result : resultList) {
				System.out
						.println("testConditionalExpressions book with non-null title: "
								+ result.getTitle());
			}
		}

		{
			Query query = em
					.createQuery("SELECT DISTINCT o FROM Order o WHERE o.lineItems IS NOT EMPTY");
			List<Order> resultList = query.getResultList();
			for (Order result : resultList) {
				System.out
						.println("testConditionalExpressions nonempty order : "
								+ result.getOrderId());
			}
		}

		{

			LineItem li = null;
			Query query = em
					.createQuery("SELECT DISTINCT i FROM LineItem i WHERE i.quantity = 9");
			query.setMaxResults(1);
			List<LineItem> resultList = query.getResultList();
			li = resultList.get(0);
			System.out
					.println("testConditionalExpressions found line item price:  "
							+ li.getPrice());

			Query query2 = em
					.createQuery("SELECT DISTINCT o FROM Order o WHERE :item MEMBER OF o.lineItems");
			query2.setParameter("item", li);
			List<Order> resultList2 = query2.getResultList();
			for (Order result : resultList2) {
				System.out
						.println("testConditionalExpressions member-of order : "
								+ result.getOrderId());
			}
		}

		{
			Query query = em
					.createQuery("SELECT DISTINCT c FROM Category c WHERE NOT EXISTS ( SELECT parentCategory  FROM Category parentCategory WHERE parentCategory = c.parentCategory)");
			List<Category> resultList = query.getResultList();
			for (Category result : resultList) {
				System.out
						.println("testConditionalExpressions Category subquery: "
								+ result.getCategoryName());
			}
		}

		{
			Query query = em
					.createQuery("SELECT li FROM LineItem li WHERE li.price > ALL (SELECT b.price FROM Book b)");
			List<LineItem> resultList = query.getResultList();
			for (LineItem result : resultList) {
				System.out
						.println("testConditionalExpressions All query line item: "
								+ result.getPrice());
			}
		}

		{
			Query query = em
					.createQuery("SELECT c FROM Category c WHERE c.createTime > ANY ( SELECT b.createTime FROM Book b WHERE b.createUser = c.createUser)");
			List<Category> resultList = query.getResultList();
			for (Category result : resultList) {
				System.out
						.println("testConditionalExpressions ANY query category: "
								+ result.getCategoryName());
			}
		}

		{
			Query query = em
					.createQuery("SELECT c FROM Customer c WHERE (SELECT AVG(o.price) FROM c.orders o) > 5");
			List<Customer> resultList = query.getResultList();
			for (Customer result : resultList) {
				System.out
						.println("testConditionalExpressions subquery customer: "
								+ result.getName());
			}
		}

		{
			Query query = em
					.createQuery("SELECT g FROM GoldCustomer g WHERE g.creditLimit > (SELECT AVG(c.creditLimit) FROM GoldCustomer c)");
			List<GoldCustomer> resultList = query.getResultList();
			for (GoldCustomer result : resultList) {
				System.out
						.println("testConditionalExpressions subquery GoldCustomer: "
								+ result.getName());
			}
		}
	}

	protected  void runAppExample() {
		System.out.flush();
		System.out.println("\nrunnig testAppExample(): ");
		Long quantity = null;
		{
			Query q = em
					.createQuery("SELECT SUM(j.quantity) AS q FROM LineItem j "
							+ "GROUP BY j.book.isbn ORDER BY q DESC");
			q.setMaxResults(1);
			q.setFirstResult(0);
			List<Long> resultList = q.getResultList();

			quantity = resultList.get(0);
			System.out.println("testAppExample book max sales price: "
					+ quantity);
		}

		{
			Query query = em.createQuery("SELECT b FROM Book b WHERE "
					+ " ( ?1 = (SELECT SUM(i.quantity) FROM LineItem i "
					+ "WHERE i.book.isbn = b.isbn GROUP BY i.book.isbn) )");
			query.setParameter(1, quantity);
			List<Book> resultList = query.getResultList();
			for (Book result : resultList) {
				System.out
						.println("Best Author Award to: " + result.getTitle());
			}
		}

		{// with jpa 2.0. It works. It only gets the ISBN, but not the title
			Query query = em
					.createQuery("SELECT SUM(i.quantity) AS q, i.book.isbn FROM LineItem i GROUP BY i.book.isbn ORDER BY q DESC");
			query.setMaxResults(1);
			query.setFirstResult(0);
			List<Object[]> resultList = query.getResultList();
			for (Object[] result : resultList) {
				Long sum = (Long) result[0];
				String isbn = (String) result[1];
				System.out.println("testAppExample book with max sales: book: "
						+ isbn + " sum:" + sum);
			}
		}
	}

	protected  void runNativeQueries() {
		System.out.flush();
		System.out.println("\nrunnig testNativeQueries(): ");

		{// native queries are polymorphic too, at least in EclipseLink.
			// Check
			// with Hibernate
			Query query = em.createNamedQuery("nativeCustomerSQL");
			query.setParameter(1, "John%");
			List<Customer> objList = query.getResultList();
			for (Customer result : objList) {
				if (result instanceof GoldCustomer) {
					System.out.println("testNativenamedQueries gold Customer: "
							+ result.getName());
				} else if (result instanceof PreferredCustomer) {
					PreferredCustomer pc = (PreferredCustomer) result;
					System.out
							.println("testNativenamedQueries preferred Customer discount rate: "
									+ pc.getDiscountRate());
				} else {
					System.out.println("testNativenamedQueries  Customer: "
							+ result.getName());
				}
			}
		}

		{
			Query query = em.createNamedQuery("nativeCustomerSQL2");
			query.setParameter(1, "John%");
			List<Customer> objList = query.getResultList();
			for (Customer result : objList) {
				if (result instanceof GoldCustomer) {
					System.out
							.println("testNativenamedQueries gold Customer 2: "
									+ result.getName());
				} else if (result instanceof PreferredCustomer) {
					PreferredCustomer pc = (PreferredCustomer) result;
					System.out
							.println("testNativenamedQueries preferred Customer discount rate2: "
									+ pc.getDiscountRate());
				} else {
					System.out.println("testNativenamedQueries  Customer2: "
							+ result.getName());
				}
			}
		}

		{// native queries are polymorphic too, at least in EclipseLink.
			// Check
			// with Hibernate
			Query query = em
					.createNativeQuery(
							"SELECT c.customer_id_pk, c.customer_type, c.name, c.income FROM customer c WHERE c.name LIKE ?1",
							Customer.class);
			query.setParameter(1, "John%");
			List<Customer> objList = query.getResultList();
			for (Customer result : objList) {
				if (result instanceof GoldCustomer) {
					System.out.println("testNativeQueries gold Customer: "
							+ result.getName());
				} else if (result instanceof PreferredCustomer) {
					PreferredCustomer pc = (PreferredCustomer) result;
					System.out
							.println("testNativeQueries preferred Customer discount rate: "
									+ pc.getDiscountRate());
				} else {
					System.out.println("testNativeQueries  Customer: "
							+ result.getName());
				}
			}
		}

		{
			Query query = em
					.createNativeQuery(
							"SELECT o.order_id_pk, o.price, o.order_time, o.status FROM Orders o, Line_Item i WHERE (i.order_id_fk = o.order_id_pk) AND i.price < ?1",
							Order.class);
			query.setParameter(1, 300);
			List<Order> objList = query.getResultList();
			for (Order result : objList) {
				System.out.println("testNativeQueries order: "
						+ result.getOrderId() + " user "
						+ result.getCreateUser());
			}
		}

		{
			Query query = em
					.createNativeQuery(
							"SELECT o.order_id_pk, o.price, o.order_time, o.status FROM Orders o, Line_Item i WHERE (i.order_id_fk = o.order_id_pk) AND i.price < ?1",
							"orderRSmapping");
			query.setParameter(1, 300);
			List<Order> objList = query.getResultList();
			for (Order result : objList) {
				System.out.println("testNativeQueries order rs mapping: "
						+ result.getOrderId() + " user "
						+ result.getCreateUser());
			}
		}

		{
			Query query = em
					.createNativeQuery(
							"SELECT o.order_id_pk, o.price, i.line_item_id_pk "
									+ "FROM Orders o, Line_Item i "
									+ "WHERE (i.order_id_fk = o.order_id_pk) AND i.price < ?1",
							"orderLineItemRSmapping");
			query.setParameter(1, 300);
			List<Object[]> objList = query.getResultList();
			for (Object[] result : objList) {
				Order order = (Order) result[0];
				LineItem li = (LineItem) result[1];

				System.out
						.println("testNativeQueries order lineitem rs mapping: "
								+ order.getOrderId()
								+ " li price "
								+ li.getPrice());
			}
		}

		{
			Query query = em
					.createNativeQuery(
							"SELECT o.order_id_pk AS order_id, o.price AS order_price, "
									+ "i.line_item_id_pk AS li_id, i.price AS li_price, i.order_id_fk, i.quantity "
									+ "FROM Orders o, Line_Item i "
									+ "WHERE (i.order_id_fk = o.order_id_pk) AND i.price < ?1",
							"orderLineItemRSmapping2");
			query.setParameter(1, 300);
			List<Object[]> objList = query.getResultList();
			for (Object[] result : objList) {
				Order order = (Order) result[0];
				LineItem li = (LineItem) result[1];

				System.out
						.println("testNativeQueries order lineitem rs mapping2: "
								+ order.getOrderId()
								+ " li price "
								+ li.getPrice());
			}
		}

		{ // mapping for returning entity and scalar
			Query query = em
					.createNativeQuery(
							"SELECT o.order_id_pk, o.price, "
									+ "i.price AS LI_PRICE "
									+ "FROM Orders o, Line_Item i "
									+ "WHERE (i.order_id_fk = o.order_id_pk) AND i.price < ?1",
							"orderLineItemRSmapping3");
			query.setParameter(1, 300);
			List<Object[]> objList = query.getResultList();
			for (Object[] result : objList) {
				Order order = (Order) result[0];
				Double price = (Double) result[1];

				System.out
						.println("testNativeQueries order lineitem rs mapping3: "
								+ order.getOrderId() + " li price " + price);
			}
		}

		{
			/*
			 * Test SQL result set mapping for composite foreign key from
			 * customer5 to Address2, and to Address3.
			 */

			// first insert some records
			Customer5 c = new Customer5();
			c.setName("John Customer");
			c.setPicture("some picture".getBytes());
			c.setIncome(100.0);

			PreferredCustomer5 p = new PreferredCustomer5();
			p.setName("John Preferred");
			p.setPicture("some picture".getBytes());
			p.setIncome(200.0);
			p.setDiscountRate(0.20);
			p.setExpirationDate(new Date());

			Address2 adp = new Address2("8920 Plum Street", "18082");
			adp.setCity("Troy");
			adp.setState("MI");
			c.setAddress2(adp);

			Address2 adp2 = new Address2("8930 Plum Street", "80822");
			adp2.setCity("Troy");
			adp2.setState("MI");
			p.setAddress2(adp2);

			BankInfo bip = new BankInfo();
			bip.setBankName("National Peach Bank");
			bip.setRoutingNumber("1234568");
			bip.setAccountNumber("99887766-3");
			p.setBank(bip);
			c.setBank(bip);

			GoldCustomer5 g = new GoldCustomer5();
			g.setName("John Gold");
			g.setPicture("some picture".getBytes());
			g.setIncome(300.0);
			g.setCardNumber("myCard#");
			g.setBank(bip);

			em.getTransaction().begin();
			em.persist(c);
			em.persist(p);
			em.persist(g);
			em.getTransaction().commit();

			// then test query with address2
			{
				Query query = em
						.createNativeQuery(
								"SELECT c.customer_id_pk, c.customer_type, c.name, "
										+ "a.street_pk, a.zip_pk "
										+ "FROM Customer5 c, Address2 a "
										+ "WHERE c.street_fk = a.street_pk AND c.zip_fk = a.zip_pk",
								"customer5Address2RSmapping");
				List<Object[]> resultList = query.getResultList();
				for (Object[] result : resultList) {
					Customer5 cust = (Customer5) result[0];
					Address2 add = (Address2) result[1];

					System.out
							.println("testNativeQueries customer5Address2RSmapping: cust name:"
									+ cust.getName()
									+ " add zip: "
									+ add.getZip());
				}
			}

			// then test query with address3
			{
				Query query = em
						.createNativeQuery(
								"SELECT c.customer_id_pk, c.customer_type, c.name, "
										+ "a.street_pk, a.zip_pk "
										+ "FROM Customer5 c, Address2 a "
										+ "WHERE c.street_fk = a.street_pk AND c.zip_fk = a.zip_pk",
								"customer5Address3RSmapping");
				List<Object[]> resultList = query.getResultList();
				for (Object[] result : resultList) {
					Customer5 cust = (Customer5) result[0];
					Address3 add = (Address3) result[1];

					System.out
							.println("testNativeQueries customer5Address3RSmapping: cust name:"
									+ cust.getName()
									+ " add zip: "
									+ add.getPk().getZip());
				}
			}
		}
	}
	
	protected  void runTypedQueries() {
		System.out.flush();
		System.out.println("\nrunnig runTypedQueries(): ");
		// return types for queries
		{
			// single select expression -> return row type: Object
			// Return an entity single result which is the order with max
			// orderId
			TypedQuery<Order> query = em
					.createQuery("SELECT o FROM Order o "
							+ "WHERE o.orderId = (SELECT MAX(o2.orderId) FROM Order o2)", Order.class);
			Order order = (Order) query.getSingleResult();

			System.out.println("typedquery  order with max orderId : " + order.getPrice());
		}

		{
			// single select expression -> return row type: Object
			// Return a scalar single result, which is the average price of
			// billed
			// orders
			// The JPQL average function AVG is used, which always returns
			// Double
			TypedQuery<Double> query = em
					.createQuery("SELECT AVG(o.price) FROM Order o WHERE o.status = :status", Double.class);
			//query.setParameter("status", OrderStatus.BILLED);
			CriteriaBuilder cb = em.getCriteriaBuilder();
			Parameter<OrderStatus> param = cb.parameter(OrderStatus.class, "status");
			query.setParameter(param, OrderStatus.BILLED);
			Double price = query.getSingleResult();

			System.out.println("typedquery with cb order average price : " + price);
		}

		{
			// A query with multiple select expressions. It returns a single
			// result
			// of type: Object[], which is a combination of entity and scalar
			Query query = em
					.createQuery("SELECT o, o.customer.bank.bankName FROM Order o "
							+ " WHERE o.orderId = (SELECT MAX(o2.orderId) FROM Order o2)");
//			Object[] result = (Object[]) query.getSingleResult();
//			Order order = (Order) result[0];
//			String bank = (String) result[1];
//
//			System.out.println("order with max orderId : price:"
//					+ order.getPrice());
//			System.out.println("order with max orderId : bank:" + bank);
		}

		// return a list of entity
		{
			// single select expression -> return row type: Object
			// Single select expression. Returns a list of Customers who ordered
			// before today
			TypedQuery<Customer> query = em
					.createQuery("SELECT o.customer FROM Order o WHERE o.orderTime < ?1", Customer.class);
			query.setParameter(1, new Date(), TemporalType.TIMESTAMP);
			List<Customer> resultList = query.getResultList();

			for (Customer result : resultList) {
				Customer customer = (Customer) result;
				if (customer != null)
				System.out.println(" typedquery Customer name: " + customer.getName());
			}
		}

		// return a list of scalar
		{
			// single select expression -> return row type: Object
			// Single select expression. Returns a list of prices with billed
			// order
			// status
			TypedQuery<Double> query = em
					.createQuery("SELECT o.price FROM Order o WHERE o.status = :status", Double.class);
			query.setParameter("status", OrderStatus.BILLED);
			List<Double> resultList = query.getResultList();

			for (Double result : resultList) {
				System.out.println(" typedquery order price: " + result);
			}
		}

		// return a list of entity and scalar
		{
			// multiple select expression -> return row type: Object[]
			// Multiple select expression. Return a list of Object[]
			
			// It does not run
//			TypedQuery<Tuple>query = em
//					.createQuery("SELECT o.customer, o.price FROM Order o WHERE o.orderTime < ?1", Tuple.class);
//			query.setParameter(1, new Date(), TemporalType.TIMESTAMP);
//			query.setMaxResults(1);
//			query.setFirstResult(0);
//			List<Tuple> resultList = query.getResultList();
//
//			for (Tuple result : resultList) {
//				Customer customer = (Customer) result.get(0, Customer.class);
//				Double price = (Double) result.get(1, Double.class);
//
//				System.out.println("typedquery customer first batch : "
//						+ customer.getName() + " price : " + price);
//			}
		}

		// constructor query
		{
			TypedQuery<OrderDetail> query = em
					.createQuery("SELECT NEW jpatest.entity.OrderDetail(o.customer.name, o.price) FROM Order o WHERE o.orderTime < ?1", OrderDetail.class);
			query.setParameter(1, new Date(), TemporalType.TIMESTAMP);
			List<OrderDetail> resultList = query.getResultList();
			for (OrderDetail result : resultList) {
				String oname = result.customer;
				double p = result.price;
				System.out.println("typedquery order detail: customer name: " + oname
						+ " price : " + p);
			}
		}
	}
		

	protected  void runQueryPerformance() {
		System.out.flush();
		System.out.println("\nrunnig testQueryPerformance(): ");

		{
			Query query = em.createQuery("SELECT o FROM Order o");

			// query = em.createNamedQuery("selectCustomerAndPrice");
			List<Order> resultList = query.getResultList();
			System.out.println("Retrieved orders:" + resultList.size());
		}
		{
			Query query = em
					.createQuery("SELECT c FROM Category c WHERE c.categoryName = ?1");

			query.setHint("eclipselink.read-only", "TRUE");
			query.setHint("eclipselink.jdbc.fetch-size", "256");
			query.setHint("javax.persistence.query.timeout", "1000");

			query.setParameter(1, "Fiction");
			List<Book> resultList = query.getResultList();
			System.out.println("Retrieved books:" + resultList.size());
		}
	}

	public static Object[] createEntities(EntityManager em) {
		Order order = new Order();
		order.setOrderTime(new Timestamp(new Date().getTime() - 10000));
		order.setPrice(100.0001);
		order.setStatus(OrderStatus.BILLED);

		Customer c = new Customer();
		c.setName("John Customer");
		c.setPicture("some picture".getBytes());
		c.setIncome(100.0);
		order.setCustomer(c);

		PreferredCustomer p = new PreferredCustomer();
		p.setName("John Preferred");
		p.setPicture("some picture".getBytes());
		p.setIncome(200.0);
		p.setDiscountRate(0.20);
		p.setExpirationDate(new Date());

		Address adp = new Address();
		adp.setStreet("2920 Peach Street");
		adp.setCity("Troy");
		adp.setState("MI");
		adp.setZip("18082");
		c.setAddress(adp);

		Address adp2 = new Address();
		adp2.setStreet("2920 Peach Street2");
		adp2.setCity("Troy");
		adp2.setState("MI");
		adp2.setZip("18082");
		p.setAddress(adp2);

		BankInfo bip = new BankInfo();
		bip.setBankName("National Peach Bank");
		bip.setRoutingNumber("1234568");
		bip.setAccountNumber("99887766-3");
		p.setBank(bip);
		c.setBank(bip);

		GoldCustomer g = new GoldCustomer();
		g.setName("John Gold");
		g.setPicture("some picture".getBytes());
		g.setIncome(300.0);
		g.setCreditLimit(2000);
		g.setCardNumber("myCard#");
		g.setAddress(adp);
		g.setBank(bip);

		GoldCustomer g2 = new GoldCustomer();
		g2.setName("John Gold2");
		g2.setPicture("some picture".getBytes());
		g2.setIncome(600.0);
		g2.setCreditLimit(4000);
		g2.setCardNumber("myCard#2");
		g2.setAddress(adp2);
		g2.setBank(bip);

		LineItem li = new LineItem();
		li.setPrice(20.00);
		li.setQuantity(9);
		// LineItem is owner of order-LineItem relationship
		// Call this setter is necessary
		li.setOrder(order);

		Book book = new Book();
		book.setIsbn("isbn233");
		book.setTitle("Java by Example");
		li.setBook(book);

		Book book2 = new Book();
		book2.setIsbn("isbn5657");
		book2.setTitle("  Java  Persistence with JPA");

		Category cat = new Category();
		cat.setCategoryName("Fiction");

		Category cat2 = new Category();
		cat2.setCategoryName("Non Fiction");
		Set<Category> cats = new HashSet<Category>();
		cats.add(cat);
		cats.add(cat2);
		book.setCategories(cats);
		book2.setCategories(cats);

		LineItem li2 = new LineItem();
		li2.setPrice(10.00);
		li2.setQuantity(7);
		li2.setBook(book2); // necessary
		li2.setOrder(order); // necessary

		// This will ensure cascade persist
		List<LineItem> lineItems = new ArrayList<LineItem>();
		lineItems.add(li);
		lineItems.add(li2);
		order.setLineItem(lineItems);

		Order order2 = new Order();
		order2.setOrderTime(new Timestamp(new Date().getTime() - 10000));
		order2.setPrice(200.002);
		order2.setStatus(OrderStatus.BILLED);
		order2.setCustomer(p);

		// create customer
		CustomerEAO custEao = new CustomerEAO();
		custEao.createCustomer(new Customer[] { c, p, g, g2 });

		// create books
		BookCategoryEAO bookEao = new BookCategoryEAO();
		List<Book> books = new ArrayList<Book>();
		books.add(book);
		books.add(book2);
		List<Category> categories = new ArrayList<Category>();
		categories.add(cat);
		categories.add(cat2);
		bookEao.createBookCategory(books, categories);

		// create order
		OrderEAO orderEao = new OrderEAO();
		orderEao.createOrder(order);
		orderEao.createOrder(order2);

		/**
		 * Test the correctness of the create operations
		 */
		Book bo = em.find(Book.class, book.getBookId());
		List<LineItem> boLIs = bo.getLineItems();

		System.out.println("\nBook has LineItems: " + boLIs.size());
		// assertTrue(0 == boLIs.size());

		{
			Query query = em
					.createQuery("SELECT b FROM Book b WHERE b.bookId = ?1");
			query.setParameter(1, book.getBookId());
			query.setHint("eclipselink.refresh", "TRUE");
			// query.setHint("eclipselink.cache-usage", "DoNotCheckCache");
			Book b2 = (Book) query.getSingleResult();
			List<LineItem> b2LIs = b2.getLineItems();
			System.out.println("\nBook has LineItems: " + b2LIs.size());

			assertTrue(1 == b2LIs.size());
			for (LineItem i : b2LIs) {
				Book b = i.getBook();
				Order o = i.getOrder();
				Customer cc = o.getCustomer();
				System.out.println("\nLineItem " + i.getLineItemId() + " for "
						+ b.getIsbn() + " is part of order with price:"
						+ o.getPrice() + " for customer: " + cc.getName());

			}
		}

		{
			Query query = em.createQuery("SELECT o FROM Order o WHERE o.customer IS NOT NULL");
			List<Order> objList = query.getResultList();
			for (Order o : objList) {
				Customer cc = o.getCustomer();
				if (cc == null || cc.getAddress() == null)
					continue;

				System.out.println("\norder " + o.getOrderId()
						+ " has customer: " + o.getCustomer()
						+ " has address: "
						+ o.getCustomer().getAddress().getCity());
			}

		}

		Object[] objs = new Object[] { order, c, books };
		return objs;
	}

}
