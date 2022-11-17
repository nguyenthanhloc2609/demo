package com.example.demo.repository;

import java.util.List;

import com.example.demo.dao.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface CustomerRepository extends MongoRepository<Customer, String> {
    @Query("{'name':{'$regex':'?0','$options':'i'}}")  
    List<Customer> findByNameLike(String name);

    Long countAllByName(String name);
}
