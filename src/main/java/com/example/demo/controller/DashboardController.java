package com.example.demo.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.example.demo.dto.StatisticCustomerDTO;
import com.example.demo.service.ICustomerService;
import com.example.demo.service.IFinanceService;

import com.example.demo.service.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    IFinanceService financeService;

    @Autowired
    ICustomerService customerService;

    @Autowired
    ITransactionService transactionService;

    @GetMapping("/finance/date")
    public ResponseEntity<?> getFinanceOnDate(@RequestParam String date) {
        return ResponseEntity.ok(financeService.getFinanceOnDay(date));
    }

    @PostMapping(value = ("/import"), consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> importExcel(MultipartHttpServletRequest request) {
        transactionService.importTransactionFromFile(request);
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
    public ResponseEntity<?> statisticCustomer() {
        StatisticCustomerDTO statisticCustomerDTO = customerService.statisticCustomer();
        return ResponseEntity.ok(statisticCustomerDTO);
    }
}
