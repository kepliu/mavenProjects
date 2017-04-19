package jpatest.test;

import javax.persistence.CacheRetrieveMode;
import javax.persistence.CacheStoreMode;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import jpatest.eao.BaseEAO;
import jpatest.entity.Address;
import jpatest.entity.Address2;
import jpatest.entity.Address2PK;
import jpatest.entity.Address3;
import jpatest.entity.Address3PK;

import org.junit.Test;

public class AddressTest extends MyJpaTestCase {

	@Test
	public void testAddress2() {
		System.out.println("testing Address2");
		Address2 created = createAddress2();
		Address2 updated = updateAddress2(created);
		deleteAddress2(updated);
	}

	@Test
	public void testAddress3() {
		System.out.println("testing Address3");
		Address3 created3 = createAddress3();
		Address3 updated3 = updateAddress3(created3);
		deleteAddress3(updated3);
	}
	
	protected Address2 createAddress2() {

		String street = "2922 Shell Drive";
		String state = "MN";
		String zip = "48091";
		Address2 address = new Address2(street, zip);
		address.setState(state);

		BaseEAO.create(address);

		/**
		 * Test the correctness of the create operations
		 */
		Address2PK apk = new Address2PK(street, zip);
		Address2 newAdd = em.find(Address2.class, apk);

		assertTrue(newAdd.getState().equals(state));
		return address;
	}

	protected Address2 updateAddress2(Address2 add) {
		String state = "MI";
		add.setState(state);
		Address2 newAdd = (Address2) BaseEAO.update(add);
		
		assertTrue(newAdd.getState().equals(state));
		return newAdd;
	}

	protected void deleteAddress2(Address2 add) {
		BaseEAO.delete(add);

		Query query = em
				.createQuery("SELECT a FROM Address2 a WHERE a.street = ?1 AND a.zip =?2");
		query.setParameter(1, add.getStreet());
		query.setParameter(2, add.getZip());
		query.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
		query.setHint("javax.persistence.cache.storeMode", CacheStoreMode.REFRESH);

		Address2 add1 = null;
		try {
			add1 = (Address2) query.getSingleResult();
		} catch (NoResultException e) {
			System.out.println("Address2 not found in database after delete. Correct");
		}
		assertTrue(add1 == null);

		Address2PK apk = new Address2PK(add.getStreet(), add.getZip());
		Address2 newAddr = em.find(Address2.class, apk);
		
		// due to caching, the deleted object may still exist here
		if (newAddr != null) {
		  System.out.println("deleted object still in this persistence context: "
				+ newAddr.getZip());
		}
	}

	protected Address3 createAddress3() {

		String street = "2923 Shell Drive";
		String state = "MN";
		String zip = "48095";
		Address3PK pk = new Address3PK(street, zip);
		Address3 add = new Address3(pk);
		add.setState(state);

		street = add.getPk().getStreet();
		zip = add.getPk().getZip();

		BaseEAO.create(add);

		/**
		 * Test the correctness of the create operations
		 */
		Address3PK apk = new Address3PK(street, zip);
		Address3 newAdd = em.find(Address3.class, apk);

		assertTrue(newAdd.getState().equals(state));
		return add;
	}

	protected Address3 updateAddress3(Address3 add) {
		String state = "MI";
		add.setState(state);
		Address3 newAdd = (Address3) BaseEAO.update(add);
		
		assertTrue(newAdd.getState().equals(state));
		return newAdd;
	}

	protected void deleteAddress3(Address3 add) {
		Address3PK apk = new Address3PK(add.getPk().getStreet(), add.getPk().getZip());

		BaseEAO.delete(add);

		Query query = em
				.createQuery("SELECT a FROM Address3 a WHERE a.pk.street = ?1 AND a.pk.zip =?2");
		query.setParameter(1, add.getPk().getStreet());
		query.setParameter(2, add.getPk().getZip());
		query.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
		Address3 add1 = null;
		try {
			add1 = (Address3) query.getSingleResult();
		} catch (NoResultException e) {
			System.out.println("Address3 not found in database after delete. Correct");
		}
		assertTrue(add1 == null);

		Address3 newAddr = em.find(Address3.class, apk);
		
		// due to caching, the deleted object may still exist here
		if (newAddr != null) {
		  System.out.println("deleted object still in this persistence context: "
				+ newAddr.getPk().getZip());
		}
	}

}
