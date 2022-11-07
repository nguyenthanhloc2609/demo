package com.example.demo.dto;

import com.example.demo.dao.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    Pagination pagination;
    Integer count;
    List<Transaction> list;
}
