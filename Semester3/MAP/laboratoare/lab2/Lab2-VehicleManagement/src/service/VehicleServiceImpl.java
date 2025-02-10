package service;

import domain.Vehicle;
import repository.VehicleRepository;

public class VehicleServiceImpl implements VehicleService {

	private VehicleRepository vehicleRepository;

	public VehicleServiceImpl(VehicleRepository vehicleRepository) {
		this.vehicleRepository = vehicleRepository;
	}

	@Override
	public Vehicle searchVehicle(String licensePlate) {
		// we will proceed with an exact search for the license plate
		//TODO add the logic for searching after license plate
//		for(Vehicle vehicle : vehicleRepository.getVehicles()){
//			if(vehicle == null || vehicle.getLicensePlate().equals(licensePlate)){
//				return vehicle;
//			}
//		}
		Vehicle[] vehicle = vehicleRepository.getVehicles();
		for(int i=0;i<vehicleRepository.getNumberOfVehicles();i++){
			if(vehicle[i].getLicensePlate().equals(licensePlate)){
				return vehicle[i];
			}
		}
		return null;
	}
}
