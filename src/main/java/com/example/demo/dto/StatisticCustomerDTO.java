package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatisticCustomerDTO {
    Long total;
    Long prepaid;
    Long postpaid;
    Long turn;
}
