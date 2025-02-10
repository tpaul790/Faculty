import domain.Vehicle;
import repository.VehicleRepository;
import utils.MileageUnit;

public class Application {

	public static void main(String[] args) {
		Vehicle vehicle = new Vehicle("BN04TIF",43.1,2022, MileageUnit.KM);
		//TODO - instantiate a vehicle
		VehicleRepository repository = new VehicleRepository();
		repository.addVehicle(vehicle);
	
		for (int i = 0; i < repository.getNumberOfVehicles(); i++) {
			Vehicle retrievedVehicle = repository.getVehicleAtPosition(i);
			//TODO -print vehicle details for retrievedVehicle object
			retrievedVehicle.printVehicleDetails();
		}
	}

}
