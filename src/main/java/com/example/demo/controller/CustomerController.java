package com.example.demo.controller;

import com.example.demo.dao.Customer;
import com.example.demo.dto.PagingDTO;
import com.example.demo.service.ICustomerService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    ICustomerService customerService;

    @PostMapping(value = "")
    public ResponseEntity<?> create(@RequestBody Customer customer) {
        Customer cu = customerService.create(customer);
        if (cu != null)
            return ResponseEntity.ok(cu);
        else
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Khách hàng đã tồn tại");
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> edit(@RequestBody Customer customer, @PathVariable String id) {
        Customer cu = customerService.update(customer, id);
        if (cu != null)
            return ResponseEntity.ok(cu);
        else
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Lỗi update");
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getOne(@PathVariable String id) {
        Customer cu = customerService.retrieve(id);
        if (cu != null)
            return ResponseEntity.ok(cu);
        else
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Lỗi");
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

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
            @RequestParam(value = "name", required = false, defaultValue = "") String name,
            @RequestParam(value = "type", required = false, defaultValue = "0") Integer type) {
        PagingDTO<Customer> customers = customerService.searchCustomer(name, type, limit, offset);
        return ResponseEntity.ok(customers);
    }

}
