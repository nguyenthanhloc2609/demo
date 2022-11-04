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
        return serviceRepository.save(service);
    }

    @Override
    public List<Service> findAll() {
        return serviceRepository.findAll();
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
        return serviceRepository.findById(id).orElse(null);
    }
}
