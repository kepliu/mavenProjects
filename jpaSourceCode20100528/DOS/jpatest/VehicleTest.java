package jpatest;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class VehicleTest {
	EntityManagerFactory emf = null;

	public VehicleTest() {
		// There is one entity manager factory per persistence unit
		emf = Persistence.createEntityManagerFactory("jpaTestPU");
	}

	public void createVehicle(Vehicle v) {
		// create a new entity manager instance from entity manager factory
		EntityManager em = emf.createEntityManager();

		try {
			em.getTransaction().begin();    // start transaction
			em.persist(v);                  // insert vehicle v into database
			em.getTransaction().commit();   // commit transaction
		} catch (Throwable t) {
			t.printStackTrace();
			em.getTransaction().rollback(); // rollback transaction
		} finally {
			em.close();                     // close entity manager
		}
	}

	public List<Vehicle> retrieveVehicles(String make) {
		EntityManager em = emf.createEntityManager();
		List<Vehicle> resultList = null;

		try {
			{
			// create a JPA query on the Java entity
			Query query = em.createQuery("SELECT v FROM Vehicle v WHERE v.make = ?1");
			query.setParameter(1, make);

			// get a list of vehicles from database for the given make
			resultList = query.getResultList();
			}

			{
			// execute a named query
			Query query = em.createNamedQuery("selectVehicleByMake");
			query.setParameter(1, make);
			
		  // get a list of vehicles from database for the given make
			resultList = query.getResultList();
			}
		} finally {
			em.close();
		}

		return resultList;
	}

	public Vehicle updateVehicle(Vehicle v) {
		EntityManager em = emf.createEntityManager();
		Vehicle v2 = null;
		try {
			em.getTransaction().begin(); // start transaction
			v2 = em.merge(v); // merge v into managed state
			
			// flush current changes to database
			em.flush();
			
			// make more changes
			v2.setYear(2011); 
			em.getTransaction().commit(); // commit transaction
		} catch (Throwable t) {
			t.printStackTrace();
			em.getTransaction().rollback(); // rollback transaction
		} finally {
			em.close(); // close entity manager
		}
		return v2;
	}
	
	public Vehicle redoChanges(Vehicle v) {
		EntityManager em = emf.createEntityManager();
		Vehicle v2 = null;
		try {
			em.getTransaction().begin(); 
			
		  // merge v into managed state
			v2 = em.merge(v);    
			
		  // undo the changes by overwriting current values
	    // by values from database
			em.refresh(v2);
			
			// make new changes from here
			v2.setYear(2011); 
			v2.setMake("GMC");
			v2.setModel("Volt");
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
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin(); // start transaction
			em.remove(em.merge(v)); // delete v from database
			em.getTransaction().commit(); // commit transaction
		} catch (Throwable t) {
			t.printStackTrace();
			em.getTransaction().rollback(); // rollback transaction
		} finally {
			em.close(); // close entity manager
		}
	}

	public Vehicle findVehicle(String vin) {
		EntityManager em = emf.createEntityManager();
		
	  // find Vehicle instance by primary key 
		Vehicle v = em.find(Vehicle.class, vin);
		
		try { 
	  	// remove v from persistence context so that it can
      // be sent to the presentation layer or a web service

		  //em.detach(v);
		} finally {
			em.close(); 
	  }

		return v;
	}

	public void close() {
		if (emf != null & emf.isOpen()) {
			emf.close();
		}
	}

	public static void main(String[] args) {
		try {
			VehicleTest vehTest = new VehicleTest();
			String vinPrimaryKey = "6B7HF16Y7SS244327";
			Vehicle v = new Vehicle(vinPrimaryKey, "Ford", "Flex", 2009);

			// insert the new Vehicle object into database
			vehTest.createVehicle(v);
			System.out.println("v.getVersion() : " + v.getVersion());
			assert v.getVersion() >= 0 : "Initial version is negative";
			if (v.getVersion() < 0)
				System.out.println("Newly inserted record version is negative");

			// do some update, then version should be incremented by 1
			v.setMake("Mercury");
			Vehicle v2 = vehTest.updateVehicle(v);
			System.out.println("v2.getVersion() : " + v2.getVersion());
			assert v2.getVersion() >= v.getVersion() + 1 : "Update version error";
			if (v2.getVersion() != v.getVersion() + 1)
				System.out.println("Optimistic lock error");
			
			// redo changes
			v2 = vehTest.redoChanges(v2);

			// retrieve all vehicles with make "Mercury"
			List<Vehicle> vehicles = vehTest.retrieveVehicles("GMC");
			for (Vehicle h : vehicles) {
				System.out.println("vehicle retrieved: " + h.getVin());
			}

			// delete newly inserted/updated vehicle. Must pass in v2 as a parameter
			// instead of v. Otherwise, OptimisticLockException would be thrown
			vehTest.deleteVehicle(v2);

			Vehicle v3 = vehTest.findVehicle(v2.getVin());
			assert v3 == null : "v3 is deleted. Must be null";
			if (v3 != null)
				System.out.println("v3 is deleted. Must be null");

			vehTest.close();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

}
