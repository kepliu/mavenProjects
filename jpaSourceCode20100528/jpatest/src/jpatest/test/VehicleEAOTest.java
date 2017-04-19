package jpatest.test;

import java.util.List;

import org.junit.Test;

import jpatest.Vehicle;
import jpatest.eao.VehicleEAO;


public class VehicleEAOTest extends MyJpaTestCase {
	@Test
	public void testVehicleEAO() {
		VehicleEAO vEao = new VehicleEAO();
		String vinPrimaryKey = "5B7HF16Y7SS244324";
		Vehicle v = new Vehicle(vinPrimaryKey, "Ford", "Flex", 2009);
		vEao.createVehicle(v);
		
		v.setMake("Mercury");
		v.setYear(2010);
		Vehicle v2 = vEao.updateVehicle(v);
		
		List<Vehicle> vehicles = vEao.retrieveVehicles("Mercury");
		for (Vehicle h: vehicles) {
			System.out.println("found vehicle: " + h.getVin());
		}

		vEao.deleteVehicle(v2);
	}

}
