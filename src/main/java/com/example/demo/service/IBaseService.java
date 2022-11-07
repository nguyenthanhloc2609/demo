package com.example.demo.service;

import com.example.demo.dto.PagingDTO;

public interface IBaseService<T> {
    T create(T t);
    PagingDTO<T> findAll(Integer limit, Integer offset);
    T update(T t, String id);
    void delete(String id);
    T retrieve(String id);
}
