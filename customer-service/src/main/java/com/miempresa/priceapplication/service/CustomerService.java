package com.miempresa.priceapplication.service;

import com.miempresa.priceapplication.exception.CustomerNotFoundException;
import com.miempresa.priceapplication.exception.CustomerServiceException;
import com.miempresa.priceapplication.model.Customer;
import com.miempresa.priceapplication.repository.CustomerRepository;
import com.miempresa.priceapplication.messaging.CustomerEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerEventPublisher eventPublisher;

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
        Customer saved = customerRepository.save(customer);
        eventPublisher.publishCustomerUpdated(saved.getId());
        return saved;
    }

    public Customer updateCustomer(Long id, Customer customer) {
        Customer existing = getCustomer(id);
        customer.setId(existing.getId());
        Customer updated = customerRepository.save(customer);
        eventPublisher.publishCustomerUpdated(updated.getId());
        return updated;
    }

    public void deleteCustomer(Long id) {
        Customer existing = getCustomer(id);
        customerRepository.delete(existing);
        eventPublisher.publishCustomerUpdated(id);
    }
}
