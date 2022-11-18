package com.example.demo.service;

import java.io.File;
import java.util.List;

import com.example.demo.dao.Finance;
import com.example.demo.dao.Spend;
import com.example.demo.dao.Transaction;
import com.example.demo.dto.PagingDTO;
import com.example.demo.repository.FinanceRepository;
import com.example.demo.repository.SpendRepository;
import com.example.demo.repository.TransactionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FinanceServiceImpl implements IFinanceService {
    @Autowired
    FinanceRepository financeRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    SpendRepository spendRepository;

    @Override
    public Finance create(Finance finance) {
        return financeRepository.save(finance);
    }

    @Override
    public PagingDTO<Finance> findAll(Integer limit, Integer offset) {
        return null;
    }

    @Override
    public Finance update(Finance finance, String id) {
        if (finance.getId().equals(id)) {
            Finance f = financeRepository.findById(id).orElse(null);
            if (f != null)
                return financeRepository.save(finance);
        }
        return null;
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public Finance retrieve(String id) {
        return null;
    }

    @Override
    public Finance getFinanceOnDay(String date) {
        Finance finance = financeRepository.getFinanceByDate(date);
        if (finance == null)
            finance = create(new Finance(date));
        return finance;
    }

    @Override
    public File exportExcel(String date) {
        // get Transaction and Spend
        List<Transaction> trans = transactionRepository.findTransactionInMonth(date.substring(3));

        List<Spend> spends = spendRepository.findSpendInMonth(date.substring(3));

        
        return null;
    }
}
