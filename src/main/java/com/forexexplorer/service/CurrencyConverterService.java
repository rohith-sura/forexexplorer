package com.forexexplorer.service;

import com.forexexplorer.model.CurrencyConverterResponse;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.time.LocalDate;

@Service
public interface CurrencyConverterService {

    public String currencyConverter(LocalDate date, String fromCurrency, String toCurrency, BigDecimal amount) throws URISyntaxException, IOException, ParseException;

}
