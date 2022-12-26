package com.example.demo.repository;

import java.util.List;

import com.example.demo.dao.Customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface CustomerRepository extends MongoRepository<Customer, String> {
    @Query("{'name':{'$regex':'?0','$options':'i'}}")
    Page<Customer> findByNameLike(String name, Pageable pageable);

    Long countAllByName(String name);

    @Query("{'name':{'$regex':'?0','$options':'i'}, 'money':{'$gt':?1}}")
    Page<Customer> searchCustomerPre(String name, Integer money, Pageable pageable);

    @Query("{'name':{'$regex':'?0','$options':'i'}, 'money':{'$lt':?1}}")
    Page<Customer> searchCustomerDebt(String name, Integer money, Pageable pageable);

    List<Customer> findByName(String name);

    Long countAllByMoneyGreaterThan(Integer value);
    
    Long countAllByMoneyLessThan(Integer value);
    
    Long countAllByMoneyEquals(Integer value);
}
