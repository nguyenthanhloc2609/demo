package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class PagingDTO<T> {
    Pagination pagination;
    Integer count;
    List<T> list;
}
