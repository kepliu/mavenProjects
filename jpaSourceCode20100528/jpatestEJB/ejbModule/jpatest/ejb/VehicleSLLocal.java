package jpatest.ejb;

import java.util.List;
import javax.ejb.Local;

import jpatest.Vehicle;

@Local
public interface VehicleSLLocal {
	
	public void createVehicle(Vehicle v);
	public Vehicle updateVehicle(Vehicle v);
	public void deleteVehicle(Vehicle v);
	public List<Vehicle> retrieveVehicles(String make);
}

