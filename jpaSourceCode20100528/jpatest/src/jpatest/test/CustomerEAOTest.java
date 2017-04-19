package jpatest.test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;


import org.junit.Test;

import jpatest.eao.CustomerEAO;
import jpatest.eao.OrderEAO;
import jpatest.entity.Address;
import jpatest.entity.BankInfo;
import jpatest.entity.Customer;
import jpatest.entity.CustomerPicture;
import jpatest.entity.GoldCustomer;
import jpatest.entity.Order;
import jpatest.entity.PreferredCustomer;

/**
 * Test Customer and its derived classes in the class hierarchy 
 * for CRUD operations for joined table customer inheritance mapping
 *
 */
public class CustomerEAOTest extends MyJpaTestCase {
	@Test
	public void testCustomer() {
		customerEAOtest();	
	}
		
	public void customerEAOtest() {
		CustomerEAO cEao = new CustomerEAO();
		Customer c = new Customer();
		c.setName("John Customer");
		c.setPicture("some picture".getBytes());
		
		CustomerPicture cp = new CustomerPicture();
		cp.setPicture("some picture".getBytes());
		cp.setCustomer(c);
		c.setCustPicture(cp);
		
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
		
		Map<Integer, Order> orders = new HashMap<Integer, Order>();
		Order o = new Order();
		o.setPrice(90.00);
		o.setCustomer(c);
		Order o2 = new Order();
		o2.setPrice(900.00);
		o2.setCustomer(c);
		orders.put(1, o);
		orders.put(2, o2);
		
				
		PreferredCustomer p = new PreferredCustomer();
		p.setName("John Preferred");
		p.setPicture("some picture".getBytes());
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
		g.setPicture("some picture".getBytes());
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
		Customer[] cs = new Customer[] {c, p, g};
		cEao.createCustomer(cs);
		cs[0].setOrders(orders);
		
		OrderEAO orderEao = new OrderEAO();
		orderEao.createOrder(o); 
		orderEao.createOrder(o2);
		
		c.setName("John Customer II");
		p.setName("John Preferred II");
		p.setDiscountRate(0.40);
		g.setName("John Gold  II");
		
		Address adp2 = new Address();
		adp2.setStreet("2920 Second  Street");
		adp2.setCity("Second Town");
		adp2.setState("MI");
		adp2.setZip("18025");
		cEao.create(adp2);
		p.setAddress(adp2);
		
		BankInfo bip2 = new BankInfo();
		bip2.setBankName("National 2nd Bank");
		bip2.setRoutingNumber("1234569");
		bip2.setAccountNumber("99887766-33");
		p.setBank(bip2);
		
		// update customers with the new values
		cs = cEao.updateCustomer(cs);
		
		// get the map of the orders for customer c. The map key is Order ID.
		Map<Integer, Order> os = cs[0].getOrders();
		Set<Integer> keys = os.keySet();
		for (Integer i: keys) {
		  Order aOrder = os.get(i);
		  System.out.println("order id: " + i);
		  assertTrue(Math.abs(aOrder.getPrice() - 90) < 0.1 || Math.abs(aOrder.getPrice() - 900) < 0.1 );
		}
		
		orderEao.deleteOrder(o);
		orderEao.deleteOrder(o2);
		cs[0].setOrders(null);
		cs[1].setOrders(null);
		cs[2].setOrders(null);
		cEao.deleteCustomer(cs);
		
		EntityManager em = cEao.createEntityManager();
		Customer c0 = em.find(Customer.class, cs[0].getCustomerId());
		assertTrue(c0 == null);
	}

}
