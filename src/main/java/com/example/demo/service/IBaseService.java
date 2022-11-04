package com.example.demo.service;

import java.util.List;

public interface IBaseService<T> {
    T create(T t);
    List<T> findAll();
    T update(T t, String id);
    void delete(String id);
    T retrieve(String id);
}
