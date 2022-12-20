package com.example.demo.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.example.demo.dto.StatisticCustomerDTO;
import com.example.demo.service.ICustomerService;
import com.example.demo.service.IFinanceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    IFinanceService financeService;

    @Autowired
    ICustomerService customerService;

    @GetMapping("/finance/date")
    public ResponseEntity<?> getFinanceOnDate(@RequestParam String date) {
        return ResponseEntity.ok(financeService.getFinanceOnDay(date));
    }

    @PostMapping("/import")
    public ResponseEntity<?> importExcel(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/excel")
    public ResponseEntity<?> exportExcel(@RequestParam String date, HttpServletResponse response) throws Exception {
        File excel = financeService.exportExcel(date);
        if (excel != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Disposition",
                    "attachment; filename=" + excel.getName().substring(0, 10) + ".xlsx");
            response.addHeader("Content-Disposition",
                    "attachment; filename=" + excel.getName().substring(0, 10) + ".xlsx");
            InputStreamResource resource = new InputStreamResource(new FileInputStream(excel));

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(excel.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

    }

    @GetMapping("statistic/customer")
    public ResponseEntity<?> statisticCustomer(){
        StatisticCustomerDTO statisticCustomerDTO = customerService.statisticCustomer();
        return ResponseEntity.ok(statisticCustomerDTO);
    }
}
