package com.example.demo.dao;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document("Customer")
public class Customer {
    @Id
    String id;

    String name;
    String billing;
    // Integer current;
    // Integer max;

    // public Customer(String name, Integer current, Integer max){
    // this.name = name;
    // this.current = current;
    // this.max = max;
    // }

    public Customer(String name, String billing) {
        this.name = name;
        this.billing = billing;
    }
}
