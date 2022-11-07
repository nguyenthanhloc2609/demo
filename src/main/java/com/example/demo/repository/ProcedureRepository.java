package com.example.demo.repository;

import com.example.demo.dao.Procedure;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProcedureRepository extends MongoRepository<Procedure, String> {
}
