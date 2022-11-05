package com.example.demo.dao;

import lombok.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Document("Transaction")
public class Transaction {
    @Id
    String id;

    String customerName;
    String listService;
    String diagnostic;
    Integer diagFee;
    String medicine;
    Integer medicineFee;
    String prepaid;
    String debt;
    String note;

    //chi
    String receivedName;
    String paidMessage;
    Integer paidMoney;
    String paidNote;

    String date;

}
