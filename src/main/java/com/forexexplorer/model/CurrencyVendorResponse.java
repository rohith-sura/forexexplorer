package com.forexexplorer.model;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class CurrencyVendorResponse {

    private Date date;
    private String base;
    private String toCurrency;
    private Map<String, Double> rates;
}
