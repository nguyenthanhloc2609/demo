package com.example.demo.dao;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("Procedure")
public class Procedure {
    @Id
    String id;

    String name;
    String code;
    Integer price;
    List<String> histories = new ArrayList<>();


    public Procedure(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public void addHistory(String his){
        histories.add(his);
    }
}


