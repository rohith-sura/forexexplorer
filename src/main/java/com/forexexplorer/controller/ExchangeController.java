package com.forexexplorer.controller;

import com.forexexplorer.dto.ConvertLatestDto;
import com.forexexplorer.model.CurrencyConverterResponse;
import com.forexexplorer.service.CurrencyConverterService;
import lombok.AllArgsConstructor;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.net.URISyntaxException;

@Controller
@AllArgsConstructor
public class ExchangeController {

    private final CurrencyConverterService currencyConverterService;

    @GetMapping(value = "/currency-converter")
    public String currencyConverter(Model model) {
        ConvertLatestDto convertLatestDto = new ConvertLatestDto();
        model.addAttribute("convertLatestDto", convertLatestDto);
        return "currency-converter";
    }

    @PostMapping(value = "/currency-converter")
    public String convertLatest(@ModelAttribute("convertLatestDto") ConvertLatestDto convertLatestDTO, Model model) throws URISyntaxException, IOException, ParseException {

        CurrencyConverterResponse response = currencyConverterService.currencyConverter(convertLatestDTO);
        model.addAttribute("currencyConverterResponse", response);
        return "convert-latest";
    }

}
