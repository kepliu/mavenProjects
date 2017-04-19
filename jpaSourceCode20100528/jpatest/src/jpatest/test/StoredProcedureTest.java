package jpatest.test;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import jpatest.eao.CustomerEAO;
import jpatest.entity.Address;
import jpatest.entity.BankInfo;
import jpatest.entity.Customer;
import jpatest.entity.CustomerType;
import jpatest.entity.GoldCustomer;
import jpatest.entity.Order;
import jpatest.entity.OrderStatus;
import jpatest.entity.PreferredCustomer;


/**
 * JPA 2.0 does not support stored procedures, although some persistence providers support it in some
 * proprietary ways.
 *
 */
public class StoredProcedureTest {
	// There is one entity manager factory per persistence unit
	EntityManagerFactory emf = null;

	public StoredProcedureTest() {
		emf = Persistence.createEntityManagerFactory("jpaTestPU");
	}

	public void close() {
		if (emf != null && emf.isOpen()) {
			emf.close();
		}
	}

	public static void main(String[] args) {
		try {
			StoredProcedureTest perTest = new StoredProcedureTest();

			perTest.test();
			perTest.close();
			
			// test a Java stored procedure
			jdbcSPTest();
			jpaSPTest();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	/**
	 * Return the number of operations (one operation is an insert, update,
	 * delete, or query)
	 */
	public void test() {
		// create a new entity manager instance from entity manager factory
		EntityManager em = emf.createEntityManager();

		try {

			int id = insertCustomers(em);

			boolean runEclipselink = true;

			if (runEclipselink) {
				System.out.println("Run EclipseLink SP Testing");
				
				// test query using SP in EclipseLink
				queryByStoredProcedureEclipseLink(em, id);

				// test delete using SP in EclipseLink
				deleteByStoredProcedureEclipseLink(em, id);

			} else {
				// test in Hibernate
				// System.out.println("Run EclipseLink SP Testing");
				// still not working
				// queryByStoredProcedureHibernate(em, id);
			}
		} catch (Throwable t) {
			t.printStackTrace();

		} finally {
			em.close(); // close entity manager
		}
	}

	protected int insertCustomers(EntityManager em) {

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

		return cs[0].getCustomerId();
	}

	protected void queryByStoredProcedureEclipseLink(EntityManager em,
			int customerID) {

		Query query = em.createNamedQuery("GET_CUSTOMER_SP");
		query.setParameter("customerID", customerID);

		Customer c = (Customer) query.getSingleResult();

		System.out.println("customer retrieved from EclipseLink SP: "
				+ c.getName());
	}

	protected void deleteByStoredProcedureEclipseLink(EntityManager em,
			int customerID) {

		em.getTransaction().begin();
		Query query = em.createNamedQuery("DELETE_CUSTOMER_SP");
		query.setParameter("customerID", customerID);

		int deleted = query.executeUpdate();
		em.getTransaction().commit();
		
		if (deleted == 1) {
			System.out.println("customer deleted from EclipseLink SP: "
					+ customerID);
		}
	}

	protected void queryByStoredProcedureHibernate(EntityManager em,
			int customerID) {

		Query query = em.createNamedQuery("GET_CUSTOMER_SP_Hibernate");
		query.setParameter("cid", customerID);

		List<Customer> c = query.getResultList();
		if (!c.isEmpty()) {
			System.out.println("customer retrieved from Hibernate SP: "
					+ c.get(0).getName());
		}
	}
	
	
	public static void jdbcSPTest() {
		System.out.println("\ntesting using JDBC ");
		try {

			Order order = new Order();
			order.setOrderTime(new Timestamp(new Date().getTime()));
			order.setPrice(10.00);
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

			EntityManagerFactory emf = Persistence
					.createEntityManagerFactory("jpaTestPU");
			EntityManager em = emf.createEntityManager();

			em.getTransaction().begin();
			em.persist(customer);
			em.persist(order);
			em.getTransaction().commit();

			int customerId = customer.getCustomerId();
			em.close();
			emf.close();

			Connection conn = DriverManager
			.getConnection("jdbc:derby://localhost:1527/jpatest","jpatest","jpatest");

			CallableStatement cs = conn.prepareCall("{ call getCustomerAndOrders(?, ?)}");
            cs.setInt(1, customerId);
            cs.registerOutParameter(2, java.sql.Types.DOUBLE);        

            boolean status = cs.execute();
            ResultSet rs  = cs.getResultSet();
            
            while (rs.next()) {
            	System.out.println("customer: " + rs.getInt(1));
            }
            
            boolean more = cs.getMoreResults();
            rs  = cs.getResultSet();
            while (rs.next()) {
            	System.out.println("order: " + rs.getInt(1));
            }
            
            Double avg = cs.getDouble(2);
            System.out.println("avg price = " + avg);


		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	
	// testing by calling a stored procedure in JPA
	public static void jpaSPTest() {
		System.out.println("\ntesting using JPA ");
		try {
			EntityManagerFactory emf = Persistence
					.createEntityManagerFactory("jpaTestPU");

			int customerId = 0;
			{

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

				EntityManager em = emf.createEntityManager();

				em.getTransaction().begin();
				em.persist(customer);
				em.persist(order);
				em.getTransaction().commit();
				customerId = customer.getCustomerId();

				em.close();
			}

			{
				EntityManager em = emf.createEntityManager();

				Query query = em.createNamedQuery("GET_CUSTOMER_ORDERS");
				query.setParameter("custID", customerId);

				List<Customer> customerList = query.getResultList();
				for (Customer c: customerList) {
					System.out.println("customer retrieved: " + c.getCustomerId());
				}

				List<Order> orderList = query.getResultList();
				for (Order o: orderList) {
					System.out.println("order retrieved: " + o.getPrice());
				}
				
				
				Double avgPrice = (Double) query.getSingleResult();

				System.out.println("customer avgPrice retrieved from EclipseLink SP: "
						+ avgPrice);

			}

			emf.close();

		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

}
