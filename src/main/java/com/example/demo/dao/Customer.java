package com.example.demo.dao;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("Customer")
public class Customer {
    @Id
    String id;

    String name;
    String billing;

    public Customer(String name, String billing) {
        this.name = name;
        this.billing = billing;
    }

    public Customer(String name) {
        this.name = name;
        this.billing = "";
    }
}
