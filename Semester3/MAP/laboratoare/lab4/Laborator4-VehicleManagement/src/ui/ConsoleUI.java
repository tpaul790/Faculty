package ui;

import java.util.List;
import java.util.Scanner;

import domain.Customer;
import domain.Vehicle;
import exceptions.NoDataAvailableException;
import repository.CustomerRepository;
import service.VehicleService;

public class ConsoleUI {

	private static final String PATH_TO_CUSTOMER_FILE = "customerInitialLoadFile";

	private VehicleService vehicleService;
	private CustomerRepository customerRepository;

	public ConsoleUI(VehicleService vehicleService, CustomerRepository customerRepository) {
		this.vehicleService = vehicleService;
		this.customerRepository = customerRepository;
	}

	public void displayVehicles() {

		try {
			List<Vehicle> vehicles = vehicleService.getAll();

			System.out.println("The available vehicles in the system are:");

			for (Vehicle vehicle : vehicles) {
				vehicle.printVehicleDetails();
			}
		} catch (NoDataAvailableException e) {
			System.out.println(e.getMessage());
		}

	}

	public void addCustomer() {
		System.out.println("Give details about a customer:");
		//TODO read customer data from the keyboard

		Scanner scanner = new Scanner(System.in);

		System.out.print("Enter customer pic: ");
		String pic = scanner.nextLine();

		System.out.print("Enter customer  name: ");
		String name = scanner.nextLine();

		System.out.print("Enter customer age: ");
		int age = scanner.nextInt();

		Customer customer = new Customer(pic, name, age);

		customerRepository.addCustomer(customer);
		scanner.close();
	}

	public void displayCustomers() {
		List<Customer> customers = customerRepository.getAllCustomers(PATH_TO_CUSTOMER_FILE);

		if (customers.size() == 0) {
			System.out.println("There are no customers in the system!");
		} else {
			System.out.println("The available customers are:");
			for (Customer c : customers) {
				System.out.println(c.toString());
			}
		}
	}

}
