package com.example.demo.controller;

import com.example.demo.dao.Spend;
import com.example.demo.dto.PagingDTO;
import com.example.demo.service.ISpendService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/spend")
public class SpendController {

    @Autowired
    ISpendService spendService;

    @PostMapping("")
    public ResponseEntity<?> create( @RequestBody Spend spend) {
        Spend sp = spendService.create(spend);
        if (sp == null)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        return ResponseEntity.ok(sp);
    }

    @GetMapping("/getByDate")
    public ResponseEntity<?> getSpendByDate(
            @RequestParam(value = "date", required = true, defaultValue = "") String date,
            @RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset) {
        PagingDTO<Spend> trans = spendService.findSpendADay(date, limit, offset);
        return ResponseEntity.ok(trans);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateSpend(@PathVariable String id, @RequestBody Spend spend){
        return null;
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable String id){
        Spend spen = spendService.retrieve(id);
        if (spen == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(spen);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteById(@PathVariable String id){
        Spend spen = spendService.retrieve(id);
        if (spen == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(spen);
    }
}
