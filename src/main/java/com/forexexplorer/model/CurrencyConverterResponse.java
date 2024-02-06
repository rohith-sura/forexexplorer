package com.forexexplorer.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class CurrencyConverterResponse {

    private String fromCurrency;
    private String toCurrency;
    private BigDecimal amount;
    private BigDecimal rate;
    private BigDecimal convertedAmount;
    private LocalDateTime dateTime;

}
