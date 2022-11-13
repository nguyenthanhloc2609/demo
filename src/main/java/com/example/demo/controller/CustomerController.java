package com.example.demo.controller;

import com.example.demo.dao.Customer;
import com.example.demo.dto.PagingDTO;
import com.example.demo.service.ICustomerService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    ICustomerService customerService;

    @PostMapping(value = "")
    public ResponseEntity<?> create(@RequestBody Customer customer) {
        return ResponseEntity.ok(customerService.create(customer));
    }

    @GetMapping("/list")
    public ResponseEntity<?> getAll(@RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset) {
        PagingDTO<Customer> customers = customerService.findAll(limit, offset);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/name")
    public ResponseEntity<?> getByName(@RequestParam(value = "name", required = false, defaultValue = "") String name) {
        List<Customer> customers = customerService.searchByName(name);
        return ResponseEntity.ok(customers);
    }

}
