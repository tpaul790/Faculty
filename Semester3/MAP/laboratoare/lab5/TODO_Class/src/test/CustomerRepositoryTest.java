package test;
import static org.junit.Assert.assertEquals;


import domain.Customer;
import org.junit.Before;
import org.junit.Test;

import repository.CustomerRepository;
import repository.CustomerRepositoryImpl;

import java.io.FileWriter;
import java.io.IOException;

public class CustomerRepositoryTest {

	private CustomerRepository customerRepository;

	@Before
	public void setUp(){
		this.customerRepository = new CustomerRepositoryImpl("customerTestLoadFile");
	}

	private void clearFileContent() {
		String filePath = "./resources/customers-test-data.txt";
		try (FileWriter writer = new FileWriter(filePath, false)) {
			writer.write("");
			System.out.println("Conținutul fișierului a fost șters.");
		} catch (IOException e) {
			System.out.println("Eroare la ștergerea conținutului fișierului: " + e.getMessage());
		}
	}
	
	@Test
	public void testAddCustomer(){
		//TODO complete the test
		setUp();
		int size = customerRepository.getAllCustomers("customerTestLoadFile").size();
        assertEquals(0, size);
		Customer customer = new Customer("5040325060028","Paul",20);
		customerRepository.addCustomer(customer);
		size = customerRepository.getAllCustomers("customerTestLoadFile").size();
		assertEquals(1, size);
		clearFileContent();
	}

	public static void main(){
		CustomerRepositoryTest test = new CustomerRepositoryTest();
		test.setUp();
		test.testAddCustomer();
	}
}
