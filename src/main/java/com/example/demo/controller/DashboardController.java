package com.example.demo.controller;

import java.io.File;

import com.example.demo.service.IFinanceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    IFinanceService financeService;

    @GetMapping("/finance/date")
    public ResponseEntity<?> getFinanceOnDate(@RequestParam String date) {
        return ResponseEntity.ok(financeService.getFinanceOnDay(date));
    }

    @GetMapping("/excel")
    public File exportExcel(@RequestParam String date) {
        return financeService.exportExcel(date);
    }
}
