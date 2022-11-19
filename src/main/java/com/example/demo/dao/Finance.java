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
    Integer countTran;
    Integer countSpend;

    public Finance(String date) {
        this.date = date;
        this.income = 0L;
        this.spend = 0L;
        this.countSpend = 0;
        this.countTran = 0;
    }

    public Integer getCountSpend() {
        return countSpend;
    }

    public String getDate() {
        return date;
    }

    public Integer getCountTran() {
        return countTran;
    }

    public void setCountSpend(Integer countSpend) {
        this.countSpend += countSpend;
    }

    public void setCountTran(Integer countTran) {
        this.countTran += countTran;
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
