package com.forexexplorer.controller;

import com.forexexplorer.service.CurrencyConverterService;
import lombok.AllArgsConstructor;
import org.apache.hc.core5.http.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.time.LocalDate;

@RestController
@AllArgsConstructor
public class ExchangeController {

    private final CurrencyConverterService currencyConverterService;

    @GetMapping(value = "/currency-converter")
    public ResponseEntity<String> currencyConverter(
            @RequestParam(value = "date") LocalDate date,
            @RequestParam(value = "fromCurrency") String fromCurrency,
            @RequestParam(value = "toCurrency") String toCurrency,
            @RequestParam(value = "amount") BigDecimal amount) throws URISyntaxException, IOException, ParseException {

        return ResponseEntity.ok(currencyConverterService.currencyConverter(date, fromCurrency, toCurrency, amount));
    }

}
