package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Pagination {
    private long offset;
    private long limit;
    private long total;
    private String next;
    private String previous;
}
