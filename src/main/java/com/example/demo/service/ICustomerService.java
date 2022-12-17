package com.example.demo.service;

import java.util.List;

import com.example.demo.dao.Customer;
import com.example.demo.dto.PagingDTO;
import com.example.demo.dto.StatisticCustomerDTO;


public interface ICustomerService extends IBaseService<Customer> {
    List<Customer> searchByName(String name);

    PagingDTO<Customer> searchCustomer(String name, Integer type, Integer limit, Integer offset);

    StatisticCustomerDTO statisticCustomer();
}
