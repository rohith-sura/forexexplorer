package com.forexexplorer.model;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class VendorLatestExchangeResponse {

    private Date date;
    private String base;
    private Map<String, Double> rates;
}
