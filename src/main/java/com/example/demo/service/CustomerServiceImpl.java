package com.example.demo.service;

import com.example.demo.dao.Customer;
import com.example.demo.dto.PagingDTO;
import com.example.demo.dto.Pagination;
import com.example.demo.repository.CustomerRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements ICustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public Customer create(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public PagingDTO<Customer> findAll(Integer limit, Integer offset) {
        Pageable pageable = PageRequest.of(offset, limit);
        Page<Customer> list = customerRepository.findAll(pageable);
        Pagination pagination = new Pagination();
        pagination.setTotal(customerRepository.count());
        pagination.setLimit(limit);
        pagination.setOffset(offset);
        return PagingDTO.<Customer>builder().pagination(pagination).list(list.getContent()).count(list.getNumberOfElements()).build();
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

    @Override
    public List<Customer> searchByName(String name) {
        System.out.println("searchByName: "+name);
        TextQuery textQuery = TextQuery.queryText(new TextCriteria().matchingAny(name)).sortByScore();
        List<Customer> result = mongoTemplate.find(textQuery, Customer.class, "customer");
        return result;
    }
}
