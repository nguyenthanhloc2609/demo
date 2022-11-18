package com.example.demo.service;

import java.io.File;

import com.example.demo.dao.Finance;

public interface IFinanceService  extends IBaseService<Finance>{
    public Finance getFinanceOnDay(String date);

    public File exportExcel(String date);
}
