package service;

import java.util.Comparator;
import java.util.List;

import domain.Customer;
import repository.CustomerRepository;

public class CustomerServiceImpl implements CustomerService{
	
	private CustomerRepository customerRepository;
	

	public CustomerServiceImpl(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}


	@Override
	public List<Customer> sortCustomersByNameAscending() {
		// TODO implement sorting by using java 8 features
		Comparator<Customer> comparator = (x,y) -> x.getName().compareTo(y.getName());
		getAll("customerInitialLoadFile").sort(comparator);
		return getAll("customerInitialLoadFile");
	}



	@Override
	public void addCustomer(Customer customer) {
		customerRepository.addCustomer(customer);		
	}



	@Override
	public List<Customer> getAll(String property) {
		return customerRepository.getAllCustomers(property);
	}

}
