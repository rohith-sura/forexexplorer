package com.forexexplorer.controller;

import com.forexexplorer.model.CurrencyConverterResponse;
import com.forexexplorer.service.CurrencyConverterService;
import lombok.AllArgsConstructor;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;

@Controller
@AllArgsConstructor
public class ExchangeController {

    private final CurrencyConverterService currencyConverterService;

    @GetMapping(value = "/currency-converter")
    public String currencyConverter(
            @RequestParam(value = "fromCurrency") String fromCurrency,
            @RequestParam(value = "toCurrency") String toCurrency,
            @RequestParam(value = "amount") BigDecimal amount, Model model) throws URISyntaxException, IOException, ParseException {

        CurrencyConverterResponse response = currencyConverterService.currencyConverter(fromCurrency, toCurrency, amount);
        model.addAttribute("currencyConverterResponse", response);
        return "currency-converter";
    }

}
