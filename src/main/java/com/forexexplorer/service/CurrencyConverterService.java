package com.forexexplorer.service;

import com.forexexplorer.model.CurrencyConverterResponse;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;

@Service
public interface CurrencyConverterService {

    public CurrencyConverterResponse currencyConverter(String fromCurrency, String toCurrency, BigDecimal amount) throws URISyntaxException, IOException, ParseException;

}
