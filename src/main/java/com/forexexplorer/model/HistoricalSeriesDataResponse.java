package com.forexexplorer.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class HistoricalSeriesDataResponse {

    private String fromCurrency;
    private String toCurrency;
    private LocalDateTime fromTime;
    private LocalDateTime toTime;
    private CurrencyConverterResponse currencyConverter;
    private String range;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal close;
    private BigDecimal percentageChange;
    private List<RateDateSeries> rateDateSeries;

}
