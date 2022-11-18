package com.example.demo.utils;

import com.example.demo.dao.Finance;
import com.example.demo.service.IFinanceService;

import org.springframework.beans.factory.annotation.Autowired;

public class FinanceUtils {
    @Autowired
    static IFinanceService financeService;

    public static void updateFinance(String date, Long income, Long spend){
        Finance finance = financeService.getFinanceOnDay(date);
        finance.setIncome(income);
        finance.setSpend(spend);
        financeService.update(finance, finance.getId());
    }
}
