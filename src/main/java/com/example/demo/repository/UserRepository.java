package com.example.demo.repository;

import com.example.demo.dao.User;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String>{
    User findUserByEmail(String email);
}
