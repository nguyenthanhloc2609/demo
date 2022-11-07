package com.example.demo.dao;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("Procedure")
public class Procedure {
    @Id
    String id;

    String name;
    String code;

    public Procedure(String id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }
}
