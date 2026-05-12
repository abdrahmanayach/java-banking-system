package service;

import exception.CustomerNotFoundException;
import model.Customer;
import repository.CustomerRepository;

import java.util.List;

public class CustomerService {
    private final CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public Customer registerCustomer(String name, String email) {
        Customer customer = new Customer(name, email);
        repository.save(customer);
        return customer;
    }

    public Customer findCustomer(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
    }

    public List<Customer> listAllCustomers() {
        return repository.findAll();
    }
}
