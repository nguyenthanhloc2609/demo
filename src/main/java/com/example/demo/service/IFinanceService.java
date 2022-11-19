package com.example.demo.service;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import com.example.demo.dao.Finance;

public interface IFinanceService  extends IBaseService<Finance>{
    public Finance getFinanceOnDay(String date);

    public File exportExcel(String date) throws IOException, InterruptedException, ExecutionException;
}
