package com.example.demo.repository;

import com.example.demo.dao.Spend;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpendRepository extends MongoRepository<Spend, String> {
    Page<Spend> getSpendByDate(String date, Pageable pageable);
}
