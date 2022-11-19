package com.example.demo.repository;

import java.util.List;

import com.example.demo.dao.Finance;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FinanceRepository extends MongoRepository<Finance, String> {
    Finance getFinanceByDate(String date);

    List<Finance> getFinanceByDateEndingWithOrderByDateAsc(String date);
}
