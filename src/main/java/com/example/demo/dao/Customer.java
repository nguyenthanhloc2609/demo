package com.example.demo.dao;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document("Customer")
public class Customer {
    @Id
    String id;

    String fullName;
    @TextIndexed(weight=5)
    String name;
    String billing;
    String note;
    String phone;
    String address;
    String diag;
    Integer money;

    public Customer(String name, String billing) {
        this.name = name;
        this.billing = billing;
        this.note = "";
    }
}
