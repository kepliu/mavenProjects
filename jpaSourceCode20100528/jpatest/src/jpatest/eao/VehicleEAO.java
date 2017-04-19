package jpatest.eao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import jpatest.Vehicle;


public class VehicleEAO extends BaseEAO{

	public void createVehicle(Vehicle v) {
		EntityManager em = createEntityManager();

		try {
			em.getTransaction().begin(); 
			em.persist(v); 
			em.getTransaction().commit(); 
		} catch (Throwable t) {
			t.printStackTrace();
			em.getTransaction().rollback(); 
		} finally {
			em.close(); 
		}
	}

	public Vehicle updateVehicle(Vehicle v) {
		EntityManager em = createEntityManager();
		Vehicle v2 = null;
		try {
			em.getTransaction().begin(); 
			v2 = em.merge(v);
			em.getTransaction().commit(); 
		} catch (Throwable t) {
			t.printStackTrace();
			em.getTransaction().rollback(); 
		} finally {
			em.close(); 
		}
		return v2;
	}

	public void deleteVehicle(Vehicle v) {
		EntityManager em = createEntityManager();
		try {
			em.getTransaction().begin(); 
			Vehicle v2 = em.merge(v); 
			em.remove(v2); 
			em.getTransaction().commit(); 
		} catch (Throwable t) {
			t.printStackTrace();
			em.getTransaction().rollback(); 
		} finally {
			em.close(); 
		}
	}

	public List<Vehicle> retrieveVehicles(String make) {
		EntityManager em = createEntityManager();
		Query query = em
				.createQuery("SELECT v FROM Vehicle AS v WHERE v.make = ?1");
		query.setParameter(1, make);
		List<Vehicle> result = query.getResultList();
		em.close();

		return result;
	}

}
