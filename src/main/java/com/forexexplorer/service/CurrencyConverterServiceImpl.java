package com.forexexplorer.service;

import com.forexexplorer.config.HttpClientConfig;
import com.forexexplorer.model.CurrencyConverterResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.net.URIBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;

import static com.forexexplorer.util.ExchangeConstants.FXRATESAPI_BASE_URL;
import static com.forexexplorer.util.ExchangeConstants.LATEST;

@Service
@AllArgsConstructor
@Slf4j
public class CurrencyConverterServiceImpl implements CurrencyConverterService{

    private final HttpClientConfig httpClient;

    @Override
    public String currencyConverter(LocalDate date, String fromCurrency, String toCurrency, BigDecimal amount) throws URISyntaxException, IOException, ParseException {
        URI uri = new URIBuilder(FXRATESAPI_BASE_URL)
                .appendPath(LATEST)
                .addParameter("base", fromCurrency)
                .addParameter("currencies", toCurrency)
                .addParameter("resolution", "1m")
                .addParameter("amount", String.valueOf(amount))
                .addParameter("places", "6")
                .addParameter("format", "json")
                .build();

        HttpGet httpGet = new HttpGet(uri);
        httpGet.addHeader("api_key", System.getenv("api_key"));

        CloseableHttpResponse response = httpClient.httpClient().execute(httpGet);
        HttpEntity entity = response.getEntity();
        String sampleResponse = EntityUtils.toString(entity);
        System.out.println(sampleResponse);

        return sampleResponse;
    }
}
