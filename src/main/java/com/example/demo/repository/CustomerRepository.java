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

    @Query("{'name':{'$regex':'?0','$options':'i'}, 'isDebtor':{'$eq':?1}}")
    Page<Customer> searchCustomer(String name, Boolean isDebtor, Pageable pageable);
}
