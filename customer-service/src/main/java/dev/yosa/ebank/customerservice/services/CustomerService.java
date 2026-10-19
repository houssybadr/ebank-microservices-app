package dev.yosa.ebank.customerservice.services;

import dev.yosa.ebank.customerservice.entities.Customer;
import dev.yosa.ebank.customerservice.repositories.CustomerRepository;
import java.util.List;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Tool(description = "Get the list of all customers")
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Tool(description = "Find a customer by its id")
    public Customer findCustomerById(@ToolParam(description = "the customer id") Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    @Tool(description = "Save a new customer (with a name and an email)")
    public Customer saveCustomer(@ToolParam(description = "the customer to save") Customer customer) {
        return customerRepository.save(customer);
    }
}
