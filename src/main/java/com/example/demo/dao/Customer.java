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
    Integer total;
    Integer current;//negative for postpaid

    public Customer(String name, Integer total, Integer current) {
        this.name = name;
        this.total = total;
        this.current = current;
    }

    public Customer(String name) {
        this.name = name;
        this.total = 0;
        this.current = 0;
    }
}
