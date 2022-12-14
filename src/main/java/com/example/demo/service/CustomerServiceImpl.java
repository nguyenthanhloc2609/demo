package com.example.demo.service;

import com.example.demo.dao.Customer;
import com.example.demo.dao.Transaction;
import com.example.demo.dto.PagingDTO;
import com.example.demo.dto.Pagination;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.TransactionRepository;

import java.util.List;
import java.util.logging.Logger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomerServiceImpl implements ICustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    TransactionRepository transactionRepository;

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
        return PagingDTO.<Customer>builder().pagination(pagination).list(list.getContent())
                .count(list.getNumberOfElements()).build();
    }

    @Override
    public Customer update(Customer customer, String id) {
        if (id.equals(customer.getId())) {
            Customer cus = customerRepository.findById(id).orElse(null);
            if (cus != null) {
                if (!cus.getName().equals(customer.getName()))
                    updateCustomerName(cus.getName(), customer.getName());

                return customerRepository.save(customer);
            }
        }
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
        Pageable pageable = PageRequest.of(0, 999999);
        Page<Customer> result = customerRepository.findByNameLike(name, pageable);
        return result.getContent();
    }

    @Override
    public PagingDTO<Customer> searchCustomer(String name, Integer type, Integer limit, Integer offset) {
        Pageable pageable = PageRequest.of(offset, limit);
        Page<Customer> list;
        if (type == 0) {
            list = customerRepository.findByNameLike(name, pageable);
        } else {
            list = customerRepository.searchCustomer(name, type == 2, pageable);

        }

        Pagination pagination = new Pagination();
        pagination.setTotal(list.getTotalElements());
        pagination.setLimit(limit);
        pagination.setOffset(offset);

        return PagingDTO.<Customer>builder().pagination(pagination).list(list.getContent())
                .count(list.getNumberOfElements()).build();
    }

    public void updateCustomerName(String name, String update) {
        List<Transaction> trans = transactionRepository.findByCustomerName(name);

        trans.forEach(tran -> tran.setCustomerName(update));
        transactionRepository.saveAll(trans);
    }
}
