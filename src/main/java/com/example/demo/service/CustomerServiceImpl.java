package com.example.demo.service;

import com.example.demo.dao.Customer;
import com.example.demo.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CustomerServiceImpl implements ICustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Override
    public Customer create(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer update(Customer customer, String id) {
        return null;
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public Customer retrieve(String id) {
        return customerRepository.findById(id).orElse(null);
    }
}
