package com.forexexplorer.service;

import com.forexexplorer.dto.ConvertLatestDto;
import com.forexexplorer.model.CurrencyConverterResponse;
import com.forexexplorer.model.HistoricalSeriesDataResponse;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;

@Service
public interface CurrencyConverterService {

    public CurrencyConverterResponse currencyConverter(ConvertLatestDto convertLatestDto) throws URISyntaxException, IOException, ParseException;

    public CurrencyConverterResponse historicalData(String toDate, String fromCurrency, String toCurrency, BigDecimal amount) throws URISyntaxException, IOException, ParseException;

    public HistoricalSeriesDataResponse seriesDataResponse(String fromCurrency, String toCurrency, String series) throws URISyntaxException, IOException, ParseException;
}
