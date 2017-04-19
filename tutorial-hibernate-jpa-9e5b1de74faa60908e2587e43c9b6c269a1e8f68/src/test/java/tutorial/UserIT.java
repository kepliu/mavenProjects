package tutorial;

import org.hibernate.LazyInitializationException;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class UserIT extends AbstractIT {
	
//	@Test
//	public void testNewUser() {
//		System.out.println("Beginning testNewUser ======================");
//		EntityManager entityManager = Persistence.createEntityManagerFactory("tutorialPU").createEntityManager();
//
//		entityManager.getTransaction().begin();
//
//		User user = new User();
//
//		user.setName(Long.toString(new Date().getTime()));
//
//		entityManager.persist(user);
//
//		entityManager.getTransaction().commit();
//
//		// see that the ID of the user was set by Hibernate
//		System.out.println("user=" + user + ", user.id=" + user.getId());
//
//		User foundUser = entityManager.find(User.class, user.getId());
//
//		// note that foundUser is the same instance as user and is a concrete
//		// class (not a JDX proxy)
//		System.out.println("foundUser =" + foundUser);
//		System.out.println("foundUser id=" + foundUser.getId() + " name = " + foundUser.getName());
//
//		assertEquals(user.getName(), foundUser.getName());
//
//		entityManager.close();
//	}
//
//
//	@Test(expected = Exception.class)
//	public void testNewUserWithTxn() throws Exception {
//		System.out.println("Beginning testNewUserWithTxn ======================");
//		EntityManager entityManager = Persistence.createEntityManagerFactory("tutorialPU").createEntityManager();
//
//		entityManager.getTransaction().begin();
//		try {
//			User user = new User();
//
//			user.setName(Long.toString(new Date().getTime()));
//
//			entityManager.persist(user);
//
//			if (true) { throw new Exception(); }
//
//			entityManager.getTransaction().commit();
//		} catch (Exception e) {
//			entityManager.getTransaction().rollback();
//			throw e;
//		}
//
//		entityManager.close();
//	}
//	
//
//	@Test 
//	public void testNewUserAndAddRole() {
//		System.out.println("Beginning testNewUserAndAddRole ======================");
//
//		EntityManager entityManager = Persistence.createEntityManagerFactory("tutorialPU").createEntityManager();
//
//		entityManager.getTransaction().begin();
//
//		User user = new User();
//
//		user.setName(Long.toString(new Date().getTime()));
//
//		Role role = new Role();
//
//		role.setName(Long.toString(new Date().getTime()));
//
//		entityManager.persist(user);
//		entityManager.persist(role);
//
//		entityManager.getTransaction().commit();
//
//		assertEquals(0, user.getRoles().size());
//
//		entityManager.getTransaction().begin();
//
//		user.addRole(role);
//
//		entityManager.merge(user);
//
//		entityManager.getTransaction().commit();
//
//		assertEquals(1, user.getRoles().size());
//
//		entityManager.close();
//	}
		

	@Test
	public void testFindUser() throws Exception {
		System.out.println("Beginning testFindUser ======================");

//		EntityManager entityManager = Persistence.createEntityManagerFactory("tutorialPU").createEntityManager();
//
//		entityManager.getTransaction().begin();
//
//		User user = new User();
//
//		String name = Long.toString(new Date().getTime());
//
//		user.setName(name);
//
//		Role role = new Role();
//		User user1 = new User();
//
//		String name1 = Long.toString(new Date().getTime());
//
//		user.setName(name);
//		user1.setName(name1);
//
//		role.setName(name);
//		user.setName(name);
//
//		Role role1 = new Role();
//
//		role1.setName(name);
//
//		user.addRole(role);
//		user.addRole(role1);
//		user1.addRole(role1);
//
//		entityManager.persist(role);
//		entityManager.persist(role1);
//		entityManager.persist(user);
//		entityManager.persist(user1);
//
//		entityManager.getTransaction().commit();
//
//		entityManager.close();

		EntityManager entityManager = Persistence.createEntityManagerFactory("tutorialPU").createEntityManager();

		 List <User> foundUser = entityManager.createNamedQuery("User.findByName", User.class).setParameter("id", 3L).getResultList();

		System.out.println("system out foundUser " + foundUser);

//		assertEquals(name, foundUser.getName());

		assertEquals(1, foundUser.size());
		for (User u : foundUser){
			for (Role r : u.getRoles()){
				System.out.println("role id: " + r.getId());
			}
		}
//		Set<Role> roles = foundUser.getRoles();
//		for (Role r : roles){
//			System.out.println("r.getName(): " + r.getName());
//			//assertEquals(role.getName(), r.getName());
//		}

//		System.out.println("foundUser.getRoles() " + foundUser.getRoles().getClass());

		entityManager.close();
	}


//	@Test(expected = LazyInitializationException.class)
//	public void testFindUser1() throws Exception {
//		System.out.println("Beginning testFindUser1 ======================");
//
//		EntityManager entityManager = Persistence.createEntityManagerFactory("tutorialPU").createEntityManager();
//
//		entityManager.getTransaction().begin();
//
//		User user = new User();
//
//		String name = Long.toString(new Date().getTime());
//
//		user.setName(name);
//
//		Role role = new Role();
//
//		role.setName(name);
//
//		user.addRole(role);
//
//		entityManager.persist(role);
//		entityManager.persist(user);
//
//		entityManager.getTransaction().commit();
//
//		entityManager.close();
//
//		entityManager = Persistence.createEntityManagerFactory("tutorialPU").createEntityManager();
//
//		User foundUser = entityManager.createNamedQuery("User.findByName", User.class).setParameter("name", name)
//				.getSingleResult();
//
//		entityManager.close();
//
//		assertEquals(1, foundUser.getRoles().size());
//
//	} 
	
}