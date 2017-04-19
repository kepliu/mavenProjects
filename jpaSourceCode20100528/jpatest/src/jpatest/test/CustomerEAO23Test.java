package jpatest.test;

import java.util.Date;

import javax.persistence.EntityManager;


import org.junit.Test;

import jpatest.eao.CustomerEAO23;
import jpatest.entity.Address;
import jpatest.entity.BankInfo;
import jpatest.entity.Customer;
import jpatest.entity.Customer2;
import jpatest.entity.Customer3;
import jpatest.entity.GoldCustomer2;
import jpatest.entity.GoldCustomer3;
import jpatest.entity.PreferredCustomer2;
import jpatest.entity.PreferredCustomer3;

/**
 * Test Customer and its derived classes in the class hierarchy 
 * for CRUD operations for single-table customer inheritance mapping
 * and for table per class inheritance.
 *
 */
public class CustomerEAO23Test extends MyJpaTestCase {
	
	
	/**
	 * Test table per class inheritance mapping
	 */
	@Test
	public void testCustomerEAO2() {
		CustomerEAO23 cEao = new CustomerEAO23();
		Customer2 c = new Customer2();
		c.setName("John Customer");
		c.setPicture("some picture".getBytes());
		
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
				
		PreferredCustomer2 p = new PreferredCustomer2();
		p.setName("John Preferred");
		p.setDiscountRate(0.20);
		p.setExpirationDate(new Date());
		p.setPicture("some picture".getBytes());
		
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
		
		GoldCustomer2 g = new GoldCustomer2();
		g.setName("John Gold");
		g.setCardNumber("myCard#");
		g.setPicture("some picture".getBytes());
		
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
		
		// create new customer2, preferred customer2, and gold customer2
		Customer2[] cs = new Customer2[] {c, p, g};
		cEao.createCustomer(cs);
		assertTrue(cs[0].getVersion() >= 0);
		
		c.setName("John Customer II");
		p.setName("John Preferred II");
		p.setDiscountRate(0.40);
		g.setName("John Gold  II");
		
		Address adp2 = new Address();
		adp2.setStreet("2920 Second  Street");
		adp2.setCity("Second Town");
		adp2.setState("MI");
		adp2.setZip("18085");
		p.setAddress(adp2);
		
		BankInfo bip2 = new BankInfo();
		bip2.setBankName("National 2nd Bank");
		bip2.setRoutingNumber("1234569");
		bip2.setAccountNumber("99887766-33");
		p.setBank(bip2);
		
		// update customers with the new values
		Customer2[] customers = cEao.updateCustomer(cs);
		assertTrue(customers[0].getVersion() == cs[0].getVersion() + 1);
						
		// delete these objects from database	
		cEao.deleteCustomer(customers);
		
		EntityManager em = cEao.createEntityManager();
		Customer c0 = em.find(Customer.class, customers[0].getCustomerId());
		assertTrue(c0 == null);
	}
	
	/**
	 * Test table per class inheritance mapping
	 */
	
	@Test
	public void testCustomerEAO3() {
		CustomerEAO23 cEao = new CustomerEAO23();
		Customer3 c = new Customer3();
		c.setName("John Customer");
		c.setPicture("some picture".getBytes());
		
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
				
		PreferredCustomer3 p = new PreferredCustomer3();
		p.setName("John Preferred");
		p.setDiscountRate(0.20);
		p.setExpirationDate(new Date());
		p.setPicture("some picture".getBytes());
		
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
		
		GoldCustomer3 g = new GoldCustomer3();
		g.setName("John Gold");
		g.setCardNumber("myCard#");
		g.setPicture("some picture".getBytes());
		
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
		
		// create new customer2, preferred customer2, and gold customer2
		Customer3[] cs = new Customer3[] {c, p, g};
		cEao.createCustomer(cs);
		assertTrue(cs[0].getVersion() >=0);
		
		c.setName("John Customer II");
		p.setName("John Preferred II");
		p.setDiscountRate(0.40);
		g.setName("John Gold  II");
		
		Address adp2 = new Address();
		adp2.setStreet("2920 Second Peach Street");
		adp2.setCity("SecondPeach Town");
		adp2.setState("MI");
		adp2.setZip("18025");
		p.setAddress(adp2);
		
		BankInfo bip2 = new BankInfo();
		bip2.setBankName("National 2nd Peach Bank");
		bip2.setRoutingNumber("1234569");
		bip2.setAccountNumber("99887766-33");
		p.setBank(bip2);
		
		// update customers with the new values
		Customer3[] customers = cEao.updateCustomer(cs);
		assertTrue(customers[0].getVersion() == cs[0].getVersion() + 1);
						
		// delete these objects from database	
		cEao.deleteCustomer(customers);
		
		EntityManager em = cEao.createEntityManager();
		Customer c0 = em.find(Customer.class, customers[0].getCustomerId());
		assertTrue(c0 == null);
	}



}
