package com.example.demo.service;

import com.example.demo.dao.Transaction;

import java.util.List;

public interface ITransactionService extends IBaseService<Transaction> {
    public void saveAll(List<Transaction> transactions);
}
