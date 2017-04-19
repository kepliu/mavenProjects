package jpatest.test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import junit.framework.TestCase;

public class MyJpaTestCase extends TestCase {
	static final EntityManagerFactory EMF = Persistence
	.createEntityManagerFactory("jpaTestPU");
	
	protected EntityManager em = null;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {	
		em = EMF.createEntityManager();
	}

	@After
	public void tearDown() throws Exception {
		if (em != null && em.isOpen()) em.close();
	}

}
