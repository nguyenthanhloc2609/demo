package com.example.demo.repository;

import com.example.demo.dao.Service;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ServiceRepository extends MongoRepository<Service, String> {
}
