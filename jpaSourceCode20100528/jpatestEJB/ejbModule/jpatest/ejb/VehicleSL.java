package jpatest.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jpatest.Vehicle;

@Stateless(mappedName = "VehicleSL")
public class VehicleSL implements VehicleSLLocal {
   
	@PersistenceContext(unitName = "jpaTestJtaPU")
	EntityManager em;

	public VehicleSL() {
	}
	
	public void createVehicle(Vehicle v) {			
		em.persist(v);           // insert into database
	}

	public List<Vehicle> retrieveVehicles(String make) {
		Query query = em
				.createQuery("SELECT v FROM Vehicle v WHERE v.make = ?1");
		query.setParameter(1, make);
		List<Vehicle> result = query.getResultList();

		return result;
	}

	public Vehicle updateVehicle(Vehicle v) {
		Vehicle v2 = em.merge(v); // merve v into managed state
		v2.setYear(2011);         // update year to 2011
		return v2;
	}
	
	public void deleteVehicle(Vehicle v) {
		em.remove(em.merge(v));   // merge v into managed state & delete
	}
	
}
