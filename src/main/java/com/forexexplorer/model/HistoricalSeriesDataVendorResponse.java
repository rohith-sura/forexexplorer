package com.forexexplorer.model;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class HistoricalSeriesDataVendorResponse {

    private String base;
    private Date start_date;
    private Date end_date;
    private Map<Date, Map<String, Double>> rates;

}
