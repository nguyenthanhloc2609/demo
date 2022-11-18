package com.example.demo.service;

import com.example.demo.dao.Finance;

public interface IFinanceService  extends IBaseService<Finance>{
    public Finance getFinanceOnDay(String date);
}
