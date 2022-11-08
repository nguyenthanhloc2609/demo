package com.example.demo.controller;

import com.example.demo.dao.Procedure;
import com.example.demo.dto.PagingDTO;
import com.example.demo.service.IProcedureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/service")
public class ProcedureController {
    @Autowired
    IProcedureService serviceService;

    @PostMapping("")
    public ResponseEntity<?> createService(@RequestBody Procedure procedure) {
        Procedure s = serviceService.create(procedure);
        return ResponseEntity.ok(s);
    }

    @GetMapping("")
    public ResponseEntity<?> findAll(@RequestParam(value = "limit", required = false, defaultValue = "20")  Integer limit,
                                     @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset) {
        PagingDTO<Procedure> list = serviceService.findAll(limit, offset);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findOne(@PathVariable String id) {
        Procedure procedure = serviceService.retrieve(id);
        if (procedure == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(procedure);
    }

}
