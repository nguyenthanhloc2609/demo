package com.example.demo.service;

import com.example.demo.dao.Transaction;
import com.example.demo.dto.PagingDTO;

import java.util.List;

public interface ITransactionService extends IBaseService<Transaction> {
    public void saveAll(List<Transaction> transactions);
    PagingDTO<Transaction> findTranADay(String date, Integer limit, Integer offset);
}
