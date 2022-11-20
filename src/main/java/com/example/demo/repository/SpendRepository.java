package com.example.demo.repository;

import java.util.List;

import com.example.demo.dao.Spend;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface SpendRepository extends MongoRepository<Spend, String> {
    Page<Spend> getSpendByDate(String date, Pageable pageable);

    Long countSpendByDate(String date);
    // @Query("{'date':{'$regex':'?0$'}}")
    // List<Spend> findSpendInMonth(String month);

    List<Spend> getSpendByDateEndingWithOrderByDateAsc(String date);
}
