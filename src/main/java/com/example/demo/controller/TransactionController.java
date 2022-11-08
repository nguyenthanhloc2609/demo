package com.example.demo.controller;

import com.example.demo.dao.Transaction;
import com.example.demo.dto.PagingDTO;
import com.example.demo.service.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    ITransactionService transactionService;

    @GetMapping("/getByDate")
    public ResponseEntity<?> getTransactionByDate(
            @RequestParam(value = "date", required = true, defaultValue = "") String date,
            @RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset) {
        PagingDTO<Transaction> trans = transactionService.findTranADay(date, limit, offset);
        return ResponseEntity.ok(trans);
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
