package com.example.demo.controller;

import com.example.demo.dao.Service;
import com.example.demo.service.IServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/service")
public class ServiceController {
    @Autowired
    IServiceService serviceService;

    @PostMapping("")
    public ResponseEntity<?> createService(@RequestBody Service service) {
        Service s = serviceService.create(service);
        return ResponseEntity.ok(s);
    }

    @GetMapping("")
    public ResponseEntity<?> findAll() {
        List<Service> list = serviceService.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findOne(@PathVariable String id) {
        Service service = serviceService.retrieve(id);
        if (service == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(service);
    }

}
