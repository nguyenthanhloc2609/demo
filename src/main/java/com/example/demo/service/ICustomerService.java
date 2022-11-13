package com.example.demo.service;

import java.util.List;

import com.example.demo.dao.Customer;

public interface ICustomerService extends IBaseService<Customer> {
    List<Customer> searchByName(String name);
}
