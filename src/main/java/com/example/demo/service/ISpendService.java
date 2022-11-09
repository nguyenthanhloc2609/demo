package com.example.demo.service;

import com.example.demo.dao.Spend;
import com.example.demo.dto.PagingDTO;

public interface ISpendService extends IBaseService<Spend> {
    PagingDTO<Spend> findSpendADay(String date, Integer limit, Integer offset);
}
