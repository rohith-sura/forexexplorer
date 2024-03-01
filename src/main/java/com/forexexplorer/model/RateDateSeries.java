package com.forexexplorer.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RateDateSeries {
    private LocalDateTime dateTime;
    private BigDecimal rate;
}
