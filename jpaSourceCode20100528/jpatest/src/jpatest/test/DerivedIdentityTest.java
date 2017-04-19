package jpatest.test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnitUtil;


import org.junit.Test;

import jpatest.entity.Address;
import jpatest.entity.Address2;
import jpatest.entity.Address3;
import jpatest.entity.Address3PK;
import jpatest.entity.Apartment1a;
import jpatest.entity.Apartment1b;
import jpatest.entity.Apartment2a;
import jpatest.entity.Apartment2b;
import jpatest.entity.Apartment3a;
import jpatest.entity.Apartment3b;
import jpatest.entity.House4a;
import jpatest.entity.House4b;
import jpatest.entity.House5a;
import jpatest.entity.House5b;
import jpatest.entity.House6a;
import jpatest.entity.House6b;

/**
 * Test JPA 2.0 new feature on derived identities
 *
 */
public class DerivedIdentityTest extends MyJpaTestCase {

	@Test
	public void testDerivedIdentities() {
		System.out.println("testing testDerivedIdentities");

		try {
			Case1a();
			Case1b();
			Case2a();
			Case2b();
			Case3a();
			Case3b();

			Case4a();
			Case4b();
			Case5a();
			Case5b();
			Case6a();
			Case6b();

		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	protected void Case1a() {
		System.out.println("testing testDerivedIdentities Case 1a");

		Address addr = new Address();
		String street = "2923 Shell Drive";
		String state = "MN";
		String zip = "48093";
		addr.setStreet(street);
		addr.setZip(zip);
		addr.setState(state);
		addr.setCity("City");

		Apartment1a apt = new Apartment1a((short) 33);
		apt.setAddrId(addr);
		apt.setRooms((short) 5);

		// insert
		em.getTransaction().begin();
		em.persist(addr);
		em.persist(apt);
		em.getTransaction().commit();
		em.clear();

		EntityManagerFactory emf = em.getEntityManagerFactory();
		PersistenceUnitUtil puUtil = emf.getPersistenceUnitUtil();

		Object id = puUtil.getIdentifier(addr);
		boolean b = puUtil.isLoaded(em.getReference(Address.class, id));
		System.out.println("address id is: " + id + " compared to: "
				+ addr.getAddressId());

		// update
		em.getTransaction().begin();
		Address addr2 = em.merge(addr);
		Apartment1a apt2 = em.merge(apt);
		addr2.setCity("myCity");
		apt2.setRooms((short) 8);
		em.getTransaction().commit();
		em.clear();

		assertTrue(apt2.getAddrId().getState().equals(addr2.getState()));
		assertTrue(apt2.getAddrId().getAddressId() == addr2.getAddressId());

		// delete
		em.getTransaction().begin();
		em.remove(em.merge(apt2));
		em.remove(em.merge(addr2));
		em.getTransaction().commit();
		em.clear();
	}

	protected void Case1b() {
		System.out.println("testing testDerivedIdentities Case 1b");

		Address addr = new Address();
		String street = "2923 Shell Drive";
		String state = "MN";
		String zip = "48093";
		addr.setStreet(street);
		addr.setZip(zip);
		addr.setState(state);
		addr.setCity("City");

		Apartment1b apt = new Apartment1b((short) 33);
		apt.setAddress(addr);
		apt.setRooms((short) 5);

		// insert
		em.getTransaction().begin();
		em.persist(addr);
		em.persist(apt);
		em.getTransaction().commit();
		em.clear();

		// update
		em.getTransaction().begin();
		Address addr2 = em.merge(addr);
		Apartment1b apt2 = em.merge(apt);
		addr2.setCity("myCity");
		apt2.setRooms((short) 8);
		em.getTransaction().commit();
		em.clear();

		assertTrue(apt2.getAddress().getState().equals(addr2.getState()));
		assertTrue(apt2.getAddress().getAddressId() == addr2.getAddressId());

		// delete
		em.getTransaction().begin();
		em.remove(em.merge(apt2));
		em.remove(em.merge(addr2));
		em.getTransaction().commit();
		em.clear();
	}

	protected void Case2a() {
		System.out.println("testing testDerivedIdentities Case 2a");

		String street = "2923 Shell Drive";
		String state = "MN";
		String zip = "48093";
		Address2 addr = new Address2(street, zip);
		addr.setState(state);
		addr.setCity("City");

		Apartment2a apt = new Apartment2a((short) 33);
		apt.setAddrId(addr);
		apt.setRooms((short) 5);

		// insert
		em.getTransaction().begin();
		em.persist(addr);
		em.persist(apt);
		em.getTransaction().commit();
		em.clear();

		// update
		em.getTransaction().begin();
		Address2 addr2 = em.merge(addr);
		Apartment2a apt2 = em.merge(apt);
		addr2.setCity("myCity");
		apt2.setRooms((short) 8);
		em.getTransaction().commit();
		em.clear();

		assertTrue(apt2.getAddrId().getState().equals(addr2.getState()));
		assertTrue(apt2.getAddrId().getZip() == addr2.getZip());

		// delete
		em.getTransaction().begin();
		em.remove(em.merge(apt2));
		em.remove(em.merge(addr2));
		em.getTransaction().commit();
		em.clear();
	}

	protected void Case2b() {
		System.out.println("testing testDerivedIdentities Case 2b");

		String street = "2923 Shell Drive";
		String state = "MN";
		String zip = "48093";
		Address2 addr = new Address2(street, zip);
		addr.setState(state);
		addr.setCity("City");

		Apartment2b apt = new Apartment2b((short) 33);
		apt.setAddress(addr);
		apt.setRooms((short) 5);

		// insert
		em.getTransaction().begin();
		em.persist(addr);
		em.persist(apt);
		em.getTransaction().commit();
		em.clear();

		// update
		em.getTransaction().begin();
		Address2 addr2 = em.merge(addr);
		Apartment2b apt2 = em.merge(apt);
		addr2.setCity("myCity");
		apt2.setRooms((short) 8);
		em.getTransaction().commit();
		em.clear();

		assertTrue(apt2.getAddress().getState().equals(addr2.getState()));
		assertTrue(apt2.getAddress().getZip() == addr2.getZip());

		// delete
		em.getTransaction().begin();
		em.remove(em.merge(apt2));
		em.remove(em.merge(addr2));
		em.getTransaction().commit();
		em.clear();
	}

	protected void Case3a() {
		System.out.println("testing testDerivedIdentities Case 3a");

		String street = "2923 Shell Drive";
		String state = "MN";
		String zip = "48093";
		Address3PK pk = new Address3PK(street, zip);
		Address3 addr = new Address3(pk);
		addr.setState(state);
		addr.setCity("City");

		Apartment3a apt = new Apartment3a((short) 33);
		apt.setAddrId(addr);
		apt.setRooms((short) 5);

		// insert
		em.getTransaction().begin();
		em.persist(addr);
		em.persist(apt);
		em.getTransaction().commit();
		em.clear();

		// update
		em.getTransaction().begin();
		Address3 addr2 = em.merge(addr);
		Apartment3a apt2 = em.merge(apt);
		addr2.setCity("myCity");
		apt2.setRooms((short) 8);
		em.getTransaction().commit();
		em.clear();

		assertTrue(apt2.getAddrId().getState().equals(addr2.getState()));
		assertTrue(apt2.getAddrId().getPk().getZip() == addr2.getPk().getZip());

		// delete
		em.getTransaction().begin();
		em.remove(em.merge(apt2));
		em.remove(em.merge(addr2));
		em.getTransaction().commit();
		em.clear();
	}

	protected void Case3b() {
		System.out.println("testing testDerivedIdentities Case 3b");

		String street = "2923 Shell Drive";
		String state = "MN";
		String zip = "48093";
		Address3PK pk = new Address3PK(street, zip);
		Address3 addr = new Address3(pk);
		addr.setState(state);
		addr.setCity("City");

		Apartment3b apt = new Apartment3b((short) 33);
		apt.setAddress(addr);
		apt.setRooms((short) 5);

		// insert
		em.getTransaction().begin();
		em.persist(addr);
		em.persist(apt);
		em.getTransaction().commit();
		em.clear();

		// update
		em.getTransaction().begin();
		Address3 addr2 = em.merge(addr);
		Apartment3b apt2 = em.merge(apt);
		addr2.setCity("myCity");
		apt2.setRooms((short) 8);
		em.getTransaction().commit();
		em.clear();

		assertTrue(apt2.getAddress().getState().equals(addr2.getState()));
		assertTrue(apt2.getAddress().getPk().getZip() == addr2.getPk().getZip());

		// delete
		em.getTransaction().begin();
		em.remove(em.merge(apt2));
		em.remove(em.merge(addr2));
		em.getTransaction().commit();
		em.clear();
	}

	protected void Case4a() {
		System.out.println("testing testDerivedIdentities Case 4a");

		Address addr = new Address();
		String street = "2923 Shell Drive";
		String state = "MN";
		String zip = "48093";
		addr.setStreet(street);
		addr.setZip(zip);
		addr.setState(state);
		addr.setCity("City");

		House4a hs = new House4a();
		hs.setAddr(addr);

		// insert
		em.getTransaction().begin();
		em.persist(addr);
		em.persist(hs);
		em.getTransaction().commit();
		em.clear();

		// update
		em.getTransaction().begin();
		Address addr2 = em.merge(addr);
		House4a hs2 = em.merge(hs);
		addr2.setCity("myCity");
		hs2.setRooms((short) 8);
		em.getTransaction().commit();
		em.clear();

		assertTrue(hs2.getAddr().getState().equals(addr2.getState()));
		assertTrue(hs2.getAddr().getAddressId() == addr2.getAddressId());

		// delete
		em.getTransaction().begin();
		em.remove(em.merge(hs2));
		em.remove(em.merge(addr2));
		em.getTransaction().commit();
		em.clear();
	}

	protected void Case4b() {
		System.out.println("testing testDerivedIdentities Case 4b");

		Address addr = new Address();
		String street = "2923 Shell Drive";
		String state = "MN";
		String zip = "48093";
		addr.setStreet(street);
		addr.setZip(zip);
		addr.setState(state);
		addr.setCity("City");

		House4b hs = new House4b();
		hs.setAddr(addr);

		// insert
		em.getTransaction().begin();
		em.persist(addr);
		em.persist(hs);
		em.getTransaction().commit();
		em.clear();

		// update
		em.getTransaction().begin();
		Address addr2 = em.merge(addr);
		House4b hs2 = em.merge(hs);
		addr2.setCity("myCity");
		hs2.setRooms((short) 8);
		em.getTransaction().commit();
		em.clear();

		assertTrue(hs2.getAddr().getState().equals(addr2.getState()));
		assertTrue(hs2.getAddr().getAddressId() == addr2.getAddressId());

		// delete
		em.getTransaction().begin();
		em.remove(em.merge(hs2));
		em.remove(em.merge(addr2));
		em.getTransaction().commit();
		em.clear();
	}

	protected void Case5a() {
		System.out.println("testing testDerivedIdentities Case 5a");

		String street = "2923 Shell Drive";
		String state = "MN";
		String zip = "48093";
		Address2 addr = new Address2(street, zip);
		addr.setState(state);
		addr.setCity("City");

		House5a hs = new House5a();
		hs.setAddr(addr);

		// insert
		em.getTransaction().begin();
		em.persist(addr);
		em.persist(hs);
		em.getTransaction().commit();
		em.clear();

		// update
		em.getTransaction().begin();
		Address2 addr2 = em.merge(addr);
		House5a hs2 = em.merge(hs);
		addr2.setCity("myCity");
		hs2.setRooms((short) 8);
		em.getTransaction().commit();
		em.clear();

		assertTrue(hs2.getAddr().getState().equals(addr2.getState()));
		assertTrue(hs2.getAddr().getZip() == addr2.getZip());

		// delete
		em.getTransaction().begin();
		em.remove(em.merge(hs2));
		em.remove(em.merge(addr2));
		em.getTransaction().commit();
		em.clear();
	}

	protected void Case5b() {
		System.out.println("testing testDerivedIdentities Case 5b");

		String street = "2923 Shell Drive";
		String state = "MN";
		String zip = "48093";
		Address2 addr = new Address2(street, zip);
		addr.setState(state);
		addr.setCity("City");

		House5b hs = new House5b();
		hs.setAddr(addr);

		// insert
		em.getTransaction().begin();
		em.persist(addr);
		em.persist(hs);
		em.getTransaction().commit();
		em.clear();

		// update
		em.getTransaction().begin();
		Address2 addr2 = em.merge(addr);
		House5b hs2 = em.merge(hs);
		addr2.setCity("myCity");
		hs2.setRooms((short) 8);
		em.getTransaction().commit();
		em.clear();

		assertTrue(hs2.getAddr().getState().equals(addr2.getState()));
		assertTrue(hs2.getAddr().getZip() == addr2.getZip());

		// delete
		em.getTransaction().begin();
		em.remove(em.merge(hs2));
		em.remove(em.merge(addr2));
		em.getTransaction().commit();
		em.clear();
	}

	protected void Case6a() {
		System.out.println("testing testDerivedIdentities Case 6a");

		String street = "2923 Shell Drive";
		String state = "MN";
		String zip = "48093";
		Address3PK pk = new Address3PK(street, zip);
		Address3 addr = new Address3(pk);
		addr.setState(state);
		addr.setCity("City");

		House6a hs = new House6a();
		hs.setAddr(addr);

		// insert
		em.getTransaction().begin();
		em.persist(addr);
		em.persist(hs);
		em.getTransaction().commit();
		em.clear();

		// update
		em.getTransaction().begin();
		Address3 addr2 = em.merge(addr);
		House6a hs2 = em.merge(hs);
		addr2.setCity("myCity");
		hs2.setRooms((short) 8);
		em.getTransaction().commit();
		em.clear();

		assertTrue(hs2.getAddr().getState().equals(addr2.getState()));
		assertTrue(hs2.getAddr().getPk().getZip() == addr2.getPk().getZip());

		// delete
		em.getTransaction().begin();
		em.remove(em.merge(hs2));
		em.remove(em.merge(addr2));
		em.getTransaction().commit();
		em.clear();
	}

	protected void Case6b() {
		System.out.println("testing testDerivedIdentities Case 6b");

		String state = "MN";
		Address3PK pk = new Address3PK("2923 Shell Drive", "48093");
		Address3 addr = new Address3(pk);
		addr.setState(state);
		addr.setCity("City");
		House6b hs = new House6b();
		hs.setAddr(addr);

		// insert
		em.getTransaction().begin();
		em.persist(addr);
		em.persist(hs);
		em.getTransaction().commit();
		em.clear();

		// update
		em.getTransaction().begin();
		Address3 addr2 = em.merge(addr);
		House6b hs2 = em.merge(hs);
		addr2.setCity("myCity");
		hs2.setRooms((short) 8);
		em.getTransaction().commit();
		em.clear();

		assertTrue(hs2.getAddr().getState().equals(addr2.getState()));
		assertTrue(hs2.getAddr().getPk().getZip() == addr2.getPk().getZip());

		// delete
		em.getTransaction().begin();
		em.remove(em.merge(hs2));
		em.remove(em.merge(addr2));
		em.getTransaction().commit();
		em.clear();
	}
}
