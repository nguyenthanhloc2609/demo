package com.example.demo.repository;

import com.example.demo.dao.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
    Page<Transaction> getTransactionByDate(String date, Pageable pageable);
}
