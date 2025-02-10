package service;

import java.util.List;
import java.util.Optional;

import domain.Vehicle;
import exceptions.NoDataAvailableException;
import repository.VehicleRepository;

public class VehicleServiceImpl implements VehicleService {
	private static final String PROPERTY_TO_LOAD_DATA="vehicleInitialLoadFile";
	
	private VehicleRepository vehicleRepository;

	public VehicleServiceImpl(VehicleRepository vehicleRepository) {
		this.vehicleRepository = vehicleRepository;
	}

	@Override
	public Vehicle searchVehicle(String licensePlate) {
		// we will proceed with an exact search for the license plate

		//TODO refactor search method to use Java 8 features
		List<Vehicle> vehicles = vehicleRepository.getVehicles();
		Optional<Vehicle> op = vehicles.stream()
				.filter(vehicle -> vehicle.getLicensePlate().equals(licensePlate))
				.findFirst();
        return op.orElse(null);
    }

	@Override
	public List<Vehicle> getAll() throws NoDataAvailableException{
	
		vehicleRepository.initialLoadOfVehicles(PROPERTY_TO_LOAD_DATA);
		
		List<Vehicle>vehicles = vehicleRepository.getVehicles();
		
		if(vehicles.size()==0){
			throw new NoDataAvailableException("There is no vehicle in the system !");
		}
		
		return vehicles;
	}
}
