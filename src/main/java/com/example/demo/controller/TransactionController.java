package com.example.demo.controller;

import com.example.demo.dao.Transaction;
import com.example.demo.dto.PagingDTO;
import com.example.demo.service.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
            @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset) {
        PagingDTO<Transaction> trans = transactionService.findTranADay(date, limit, offset);
        return ResponseEntity.ok(trans);
    }

    @GetMapping("/getByMonth")
    public ResponseEntity<?> getTransactionByMonth(Integer month) {
        return null;
    }

    @PostMapping("")
    public ResponseEntity<?> addTransaction(@RequestBody Transaction tran) {
        Transaction transaction = transactionService.create(tran);
        if (transaction == null)
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        return ResponseEntity.ok(transaction);
    }

    @PostMapping("/addList")
    public ResponseEntity<?> addTransactions(@RequestBody List<Transaction> trans) {
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTransactionById(@PathVariable String id) {
        Transaction tran = transactionService.retrieve(id);
        if (tran != null)
            return ResponseEntity.ok(tran);
        else
            return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTransactionById(@PathVariable String id, @RequestBody Transaction tran) {
        Transaction updated = transactionService.update(tran, id);
        if (updated != null)
            return ResponseEntity.ok(tran);
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTransactionById(@PathVariable String id) {
        transactionService.delete(id);
        return ResponseEntity.ok("Delete successfully");
    }
}
