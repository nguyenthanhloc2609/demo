package com.example.demo.service;

import com.example.demo.dao.Service;
import com.example.demo.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@org.springframework.stereotype.Service
public class ServiceServiceImpl implements IServiceService {
    @Autowired
    ServiceRepository serviceRepository;

    @Override
    public Service create(Service service) {
        return null;
    }

    @Override
    public List<Service> findAll() {
        return null;
    }

    @Override
    public Service update(Service service, String id) {
        return null;
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public Service retrieve(String id) {
        return null;
    }
}
