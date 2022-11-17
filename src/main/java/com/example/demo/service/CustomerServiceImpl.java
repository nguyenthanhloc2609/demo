package com.example.demo.service;

import com.example.demo.dao.Customer;
import com.example.demo.dto.PagingDTO;
import com.example.demo.dto.Pagination;
import com.example.demo.repository.CustomerRepository;

import java.util.List;
import java.util.logging.Logger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomerServiceImpl implements ICustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    Logger logger = Logger.getLogger(CustomerServiceImpl.class.getName());

    @Override
    public Customer create(Customer customer) {
        long count = customerRepository.countAllByName(customer.getName());
        if (count == 0)
            return customerRepository.save(customer);
        else {
            log.error("Khách hàng có tên " + customer.getName() + " đã tồn tại!");
            return null;
        }
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
        // System.out.println("searchByName: "+name);
        // TextQuery textQuery = TextQuery.queryText(new TextCriteria().matchingAny(name)).sortByScore();
        // List<Customer> result = mongoTemplate.find(textQuery, Customer.class, "customer");
        List<Customer> result = customerRepository.findByNameLike(name);
        return result;
    }
}
