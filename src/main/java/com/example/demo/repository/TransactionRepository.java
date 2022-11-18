package com.example.demo.repository;

import java.util.List;

import com.example.demo.dao.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
    Page<Transaction> getTransactionByDate(String date, Pageable pageable);

    @Query("{'date':{'$regex':'?0$'}}")
    List<Transaction> findTransactionInMonth(String month);
}
