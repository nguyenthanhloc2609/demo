package com.example.demo.service;

import com.example.demo.dao.Procedure;
import com.example.demo.dto.Pagination;
import com.example.demo.dto.PagingDTO;
import com.example.demo.repository.ProcedureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProcedureServiceImpl implements IProcedureService {
    @Autowired
    ProcedureRepository procedureRepository;

    @Override
    public Procedure create(Procedure procedure) {
        return procedureRepository.save(procedure);
    }

    @Override
    public PagingDTO<Procedure> findAll(Integer limit, Integer offset) {
        Pageable pageable = PageRequest.of(offset, limit);
        Page<Procedure> list = procedureRepository.findAll(pageable);
        Pagination pagination = new Pagination();
        pagination.setTotal(procedureRepository.count());
        pagination.setLimit(limit);
        pagination.setOffset(offset);
        return PagingDTO.<Procedure>builder().pagination(pagination).list(list.getContent()).count(list.getNumberOfElements()).build();
    }

    @Override
    public Procedure update(Procedure procedure, String id) {
        return null;
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public Procedure retrieve(String id) {
        return procedureRepository.findById(id).orElse(null);
    }
}
