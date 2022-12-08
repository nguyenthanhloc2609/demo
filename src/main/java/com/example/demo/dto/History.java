package com.example.demo.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

public class History {
    Integer price;
    String date;
    

    public History(Integer price) {
        this.price = price;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        this.date = sdf.format(new Date());
    }
}