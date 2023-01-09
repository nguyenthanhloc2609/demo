package com.example.demo.dao;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Document("User")
public class User {
    @Id
    private String id;

    private String name;
    private String email;
    private String password;

}
