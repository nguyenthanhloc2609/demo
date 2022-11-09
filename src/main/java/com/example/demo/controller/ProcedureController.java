package com.example.demo.controller;

import com.example.demo.dao.Procedure;
import com.example.demo.dto.PagingDTO;
import com.example.demo.service.IProcedureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/procedure")
public class ProcedureController {
    @Autowired
    IProcedureService procedureService;

    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody Procedure procedure) {
        Procedure s = procedureService.create(procedure);
        return ResponseEntity.ok(s);
    }

    @GetMapping("/list")
    public ResponseEntity<?> findAll(@RequestParam(value = "limit", required = false, defaultValue = "20")  Integer limit,
                                     @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset) {
        PagingDTO<Procedure> list = procedureService.findAll(limit, offset);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findOne(@PathVariable String id) {
        Procedure procedure = procedureService.retrieve(id);
        if (procedure == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(procedure);
    }

}
