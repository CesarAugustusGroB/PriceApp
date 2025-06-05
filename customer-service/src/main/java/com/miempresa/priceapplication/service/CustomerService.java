package com.miempresa.priceapplication.service;

import com.miempresa.priceapplication.exception.CustomerNotFoundException;
import com.miempresa.priceapplication.exception.CustomerServiceException;
import com.miempresa.priceapplication.model.Customer;
import com.miempresa.priceapplication.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;

    public List<Customer> getAllCustomers() {
        log.debug("Fetching all customers");
        return customerRepository.findAll();
    }

    public Customer getCustomer(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer with id " + id + " not found"));
    }

    public Customer createCustomer(Customer customer) {
        log.debug("Creating customer: {}", customer);
        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new CustomerServiceException("Email already exists");
        }
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Long id, Customer customer) {
        Customer existing = getCustomer(id);
        customer.setId(existing.getId());
        return customerRepository.save(customer);
    }

    public void deleteCustomer(Long id) {
        Customer existing = getCustomer(id);
        customerRepository.delete(existing);
    }
}
