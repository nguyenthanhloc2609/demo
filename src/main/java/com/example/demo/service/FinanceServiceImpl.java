package com.example.demo.service;

import com.example.demo.dao.Finance;
import com.example.demo.dto.PagingDTO;
import com.example.demo.repository.FinanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FinanceServiceImpl implements IFinanceService {
    @Autowired
    FinanceRepository financeRepository;

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
}
