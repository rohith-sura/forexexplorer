package com.forexexplorer.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CurrencyConverterResponse {

    private LocalDate fromDate;
    private LocalDate toDate;
    private String fromCurrency = "";
    private String toCurrency;
    private String statusMessage;
    private String statusCode;
    private BigDecimal convertedAmount;
    private BigDecimal rate;
    private LocalDateTime timestamp;

}
