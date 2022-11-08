package com.example.demo.repository;

import com.example.demo.dao.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, String> {
//    Page<UserEntity> findAllByFullNameOrUsernameOrEmail(String keyword, Pageable pageable);
}
