package com.example.demo.controller;

import com.example.demo.dao.Transaction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @GetMapping("/getByDate")
    public ResponseEntity<?> getTransactionByDate(String date) {
        return null;
    }

    @GetMapping("/getByMonth")
    public ResponseEntity<?> getTransactionByMonth(Integer month) {
        return null;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addTransaction(@RequestBody Transaction tran) {
        return null;
    }

    @PostMapping("")
    public ResponseEntity<?> addTransactions(@RequestBody List<Transaction> trans) {
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTransactionById(Integer month) {
        return null;
    }
}
