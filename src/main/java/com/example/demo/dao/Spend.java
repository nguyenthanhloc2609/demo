package com.example.demo.dao;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document("Spend")
public class Spend {

    @Id
    String id;

    String name;
    Integer money;
    String detail;
    String note;
    String date;

    // public Spend(String name, Integer money, String detail, String note) {
    //     this.name = name;
    //     this.money = money;
    //     this.detail = detail;
    //     this.note = note;
    // }
}
