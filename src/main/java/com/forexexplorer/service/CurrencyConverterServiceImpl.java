package com.forexexplorer.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.forexexplorer.config.HttpClientConfig;
import com.forexexplorer.model.CurrencyConverterResponse;
import com.forexexplorer.model.VendorLatestExchangeResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.net.URIBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

import static com.forexexplorer.util.ExchangeConstants.FXRATESAPI_BASE_URL;
import static com.forexexplorer.util.ExchangeConstants.LATEST;

@Service
@AllArgsConstructor
@Slf4j
public class CurrencyConverterServiceImpl implements CurrencyConverterService{

    private final HttpClientConfig httpClient;

    @Override
    public CurrencyConverterResponse currencyConverter(String fromCurrency, String toCurrency, BigDecimal amount) throws URISyntaxException, IOException, ParseException {
        URI uri = new URIBuilder(FXRATESAPI_BASE_URL)
                .appendPath(LATEST)
                .addParameter("base", fromCurrency)
                .addParameter("currencies", toCurrency)
                .addParameter("resolution", "1m")
                .addParameter("amount", "1")
                .addParameter("places", "2")
                .addParameter("format", "json")
                .build();
        HttpGet httpGet = new HttpGet(uri);
        httpGet.addHeader("api_key", System.getenv("api_key"));
        CloseableHttpResponse response = httpClient.httpClient().execute(httpGet);

        CurrencyConverterResponse converterResponse = null;
        if (response.getCode() == 200) {
            String responseEntity = EntityUtils.toString(response.getEntity());
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            VendorLatestExchangeResponse vendorResponse = mapper.readValue(responseEntity, VendorLatestExchangeResponse.class);
            if (vendorResponse != null) {
                Map<String, Double> rates = vendorResponse.getRates();
                Double drate = rates.get(toCurrency);
                BigDecimal rate = BigDecimal.valueOf(drate);
                LocalDateTime localDateTime = vendorResponse.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                converterResponse = CurrencyConverterResponse.builder()
                        .fromCurrency(vendorResponse.getBase())
                        .toCurrency(toCurrency)
                        .amount(amount)
                        .rate(rate)
                        .convertedAmount(amount.multiply(rate))
                        .dateTime(localDateTime)
                        .build();
            }
        }
        return converterResponse;
    }
}
