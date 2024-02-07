package com.forexexplorer.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ConvertLatestDto {
    private String fromCurrency;
    private String toCurrency;
    private BigDecimal amount;
}
