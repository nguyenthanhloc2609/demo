package com.example.demo.service;

import com.example.demo.dao.Spend;
import com.example.demo.dao.Transaction;
import com.example.demo.dto.PagingDTO;
import com.example.demo.repository.SpendRepository;
import com.example.demo.dto.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Service
public class SpendServiceImpl implements ISpendService {

    @Autowired
    SpendRepository spendRepository;

    @Override
    public Spend create(Spend t) {
        // TODO Auto-generated method stub
        return spendRepository.save(t);
    }

    @Override
    public PagingDTO<Spend> findAll(Integer limit, Integer offset) {
        Pageable pageable = PageRequest.of(offset, limit);
        Page<Spend> list = spendRepository.findAll(pageable);
        Pagination pagination = new Pagination();
        pagination.setTotal(spendRepository.count());
        pagination.setLimit(limit);
        pagination.setOffset(offset);
        return PagingDTO.<Spend>builder().pagination(pagination).list(list.getContent()).count(list.getNumberOfElements()).build();
    }

    @Override
    public Spend update(Spend t, String id) {
        // TODO Auto-generated method stub
        if (t.getId().equals(id)) {
            Spend s = spendRepository.findById(id).orElse(null);
            if (s != null)
                return spendRepository.save(t);
        }
        return null;
    }

    @Override
    public void delete(String id) {
        // TODO Auto-generated method stub
        spendRepository.findById(id).ifPresent(tran -> spendRepository.delete(tran));
    }

    @Override
    public Spend retrieve(String id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PagingDTO<Spend> findSpendADay(String date, Integer limit, Integer offset) {
        // TODO Auto-generated method stub
        return null;
    }

}
