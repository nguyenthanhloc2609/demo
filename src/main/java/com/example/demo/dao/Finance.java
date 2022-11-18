package com.example.demo.dao;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Finance")
public class Finance {
    @Id
    String id;

    String date;
    Long income;
    Long spend;

    public Finance(String date) {
        this.date = date;
        this.income = 0L;
        this.spend = 0L;
    }

    public String getId() {
        return id;
    }

    public void setIncome(Long income) {
        this.income += income;
    }

    public void setSpend(Long spend) {
        this.spend += spend;
    }

    public Long getIncome() {
        return income;
    }

    public Long getSpend() {
        return spend;
    }

    public Long getProfit() {
        return this.income - this.spend;
    }
}
