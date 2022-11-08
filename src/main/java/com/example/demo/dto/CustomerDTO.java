package com.example.demo.dto;

import lombok.Data;

@Data
public class CustomerDTO {
    
    String name;
    String billing;

    public CustomerDTO(String name, String billing) {
        this.name = name;
        this.billing = billing;
    }

    public CustomerDTO(String name) {
        this.name = name;
        this.billing = "";
    }
}
