package com.example.demo.dao;

import lombok.Builder;
import lombok.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Builder
@Document("Transaction")
public class Transaction {
    @Id
    String id;

    String customerName;
    String listProcedure;
    String diagnostic;
    Integer proceMoney;
    String medicine;
    Integer medicineMoney;
    String prepaid;
    String debt;
    String note;

    String date;

}
