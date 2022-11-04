package com.example.demo.dao;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("Service")
public class Service {
    @Id
    String id;

    String name;
    String code;

    public Service(String id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }
}
