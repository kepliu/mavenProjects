package jpatest.test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.PersistenceUtil;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;


import org.junit.Test;

import jpatest.entity.Address;
import jpatest.entity.BankInfo;
import jpatest.entity.Customer;
import jpatest.entity.LineItem;
import jpatest.entity.Order;
import jpatest.entity.OrderStatus;

public class MiscellaneousTest extends MyJpaTestCase {

	@Test
	public void testAll() {
		// test load state
		loadState();

		// test element collection
		Customer c = elementCollectionCreate();
		elementCollectionDelete(c);

		// test programmatic validation
		programmaticValidation();
	}

	public void loadState() {
		System.out.println("testing LoadState");

		try {

			Order ord = new Order();
			ord.setOrderTime(new Timestamp(new Date().getTime() - 10000));
			ord.setPrice(100.30);
			ord.setStatus(OrderStatus.BILLED);

			em.getTransaction().begin();
			em.persist(ord);
			int orderId = ord.getOrderId();
			em.getTransaction().commit();

			{
				EntityManagerFactory emf = em.getEntityManagerFactory();
				PersistenceUnitUtil puUtil = emf.getPersistenceUnitUtil();
				Order orderRef = em.getReference(Order.class, orderId);
				boolean b = puUtil.isLoaded(orderRef);
				boolean bPrice = puUtil.isLoaded(orderRef, "price");

				Order order = em.find(Order.class, orderId);
				boolean bli = puUtil.isLoaded(order, "lineItems");
				List<LineItem> li = order.getLineItems();
				boolean bli2 = puUtil.isLoaded(order, "lineItems");
			}

			{
				PersistenceUtil util = Persistence.getPersistenceUtil();
				boolean b20 = util.isLoaded(em.getReference(Order.class,
						orderId));
				boolean b22 = util.isLoaded(em.find(Order.class, 123),
						"lineItems");
			}
			{// getReference method

				LineItem item = new LineItem();
				item.setPrice(34.95);
				item.setQuantity(100);

				em.getTransaction().begin();
				Order order = em.getReference(Order.class, orderId);
				item.setOrder(order);
				order.getLineItems().add(item);
				em.persist(item);
				em.getTransaction().commit();
			}

		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public Customer elementCollectionCreate() {
		em = EMF.createEntityManager();
		System.out.println("testing element Collection");
		Customer rc = new Customer();
		rc.setName("John Customer");
		rc.setPicture("some picture".getBytes());

		Set<String> nickNames = new HashSet<String>();
		nickNames.add("Johnny");
		nickNames.add("Tony");
		rc.setNickNames(nickNames);

		List<BankInfo> bankInfos = new ArrayList<BankInfo>();
		BankInfo bi = new BankInfo();
		bi.setBankName("NationalBank");
		bi.setRoutingNumber("1234567");
		bi.setAccountNumber("99887766-2");
		rc.setBank(bi);
		bankInfos.add(bi);

		BankInfo big = new BankInfo();
		big.setBankName("National Gold Bank");
		big.setRoutingNumber("1234568");
		big.setAccountNumber("99887766-4");
		bankInfos.add(big);

		rc.setBankInfos(bankInfos);

		Address adp = new Address();
		adp.setStreet("2920 Peach Street");
		adp.setCity("Peach Town");
		adp.setState("MI");
		adp.setZip("18082");
		rc.setAddress(adp);

		// insert
		em.getTransaction().begin();
		em.persist(rc);
		em.getTransaction().commit();
		em.clear();

		return rc;
	}

	public void elementCollectionDelete(Customer rc) {
		// delete
		em.getTransaction().begin();
		em.remove(em.merge(rc));
		em.getTransaction().commit();
		em.clear();
	}

	public void programmaticValidation() {
		System.out.println("testing programmaticValidation");

		Customer rc = new Customer();
		rc.setName("John Customer");
		rc.setPicture("some picture".getBytes());

		Set<String> nickNames = new HashSet<String>();
		nickNames.add("Johnny");
		nickNames.add("Tony");
		rc.setNickNames(nickNames);

		List<BankInfo> bankInfos = new ArrayList<BankInfo>();
		BankInfo bi = new BankInfo();
		bi.setBankName("NationalBank");
		bi.setRoutingNumber("1234567");
		bi.setAccountNumber("99887766-2");
		bankInfos.add(bi);

		BankInfo big = new BankInfo();
		big.setBankName("National Gold Bank");
		big.setRoutingNumber("1234568");
		big.setAccountNumber("99887766-4");
		bankInfos.add(big);

		rc.setBankInfos(bankInfos);

		Address adp = new Address();
		adp.setStreet("2920 Peach Street");
		adp.setCity("Peach Town");
		adp.setState("MI");
		adp.setZip("18082");
		rc.setAddress(adp);

		// Programmatic validation
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();

		validator.validate(adp);
		validator.validate(rc);

	}

}
