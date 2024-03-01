package com.forexexplorer.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.forexexplorer.config.HttpClientConfig;
import com.forexexplorer.dto.ConvertLatestDto;
import com.forexexplorer.model.*;
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
import java.time.ZoneOffset;
import java.util.*;

import static com.forexexplorer.util.ExchangeConstants.*;
import static com.forexexplorer.util.Utility.*;

@Service
@AllArgsConstructor
@Slf4j
public class CurrencyConverterServiceImpl implements CurrencyConverterService{

    private final HttpClientConfig httpClient;

    @Override
    public CurrencyConverterResponse currencyConverter(ConvertLatestDto convertLatestDto) throws URISyntaxException, IOException, ParseException {
        URI uri = new URIBuilder(FXRATESAPI_BASE_URL)
                .appendPath(LATEST)
                .addParameter("base", convertLatestDto.getFromCurrency())
                .addParameter("currencies", convertLatestDto.getToCurrency())
                .addParameter("resolution", "1m")
                .addParameter("places", "2")
                .build();
        HttpGet httpGet = new HttpGet(uri);
        httpGet.addHeader(API_KEY, System.getenv("api_key"));
        CloseableHttpResponse response = httpClient.httpClient().execute(httpGet);

        CurrencyConverterResponse converterResponse = null;
        if (response.getCode() == 200) {
            String responseEntity = EntityUtils.toString(response.getEntity());
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            CurrencyVendorResponse vendorResponse = mapper.readValue(responseEntity, CurrencyVendorResponse.class);
            if (vendorResponse != null) {
                Map<String, Double> rates = vendorResponse.getRates();
                LocalDateTime localDateTime = vendorResponse.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                converterResponse = CurrencyConverterResponse.builder()
                        .fromCurrency(vendorResponse.getBase())
                        .toCurrency(convertLatestDto.getToCurrency())
                        .amount(convertLatestDto.getAmount())
                        .rate(doubleToBigDecimal(rates.get(convertLatestDto.getToCurrency())))
                        .convertedAmount(convertLatestDto.getAmount().multiply(doubleToBigDecimal(rates.get(convertLatestDto.getToCurrency()))))
                        .dateTime(localDateTime)
                        .build();
            }
        }
        return converterResponse;
    }

    @Override
    public CurrencyConverterResponse historicalData(String toDate, String fromCurrency, String toCurrency, BigDecimal amount) throws URISyntaxException, IOException, ParseException {

        URI uri = new URIBuilder(FXRATESAPI_BASE_URL)
                .appendPath(HISTORICAL_DATA)
                .addParameter(API_KEY, System.getenv("api_key"))
                .addParameter("date", toDate)
                .addParameter("base", fromCurrency)
                .addParameter("currencies", toCurrency)
                .addParameter("resolution", "1m")
                .addParameter("places", "2")
                .build();

        HttpGet get = new HttpGet(uri);
        CloseableHttpResponse vendorResponse = httpClient.httpClient().execute(get);
        CurrencyConverterResponse converterResponse = null;
        if(vendorResponse.getCode() == 200) {
            String responseEntity = EntityUtils.toString(vendorResponse.getEntity());
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            CurrencyVendorResponse response = mapper.readValue(responseEntity, CurrencyVendorResponse.class);
            if (response!=null) {
                Map<String, Double> rates = response.getRates();
                LocalDateTime localDateTime = response.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                converterResponse = CurrencyConverterResponse.builder()
                        .fromCurrency(response.getBase())
                        .toCurrency(toCurrency)
                        .rate(doubleToBigDecimal(rates.get(toCurrency)))
                        .convertedAmount(amount.multiply(doubleToBigDecimal(rates.get(toCurrency))))
                        .dateTime(localDateTime)
                        .amount(amount)
                        .build();
            }
        }
        return converterResponse;
    }

    @Override
    public HistoricalSeriesDataResponse seriesDataResponse(String fromCurrency, String toCurrency, String series) throws URISyntaxException, IOException, ParseException {
        LocalDateTime currentTime = LocalDateTime.now((ZoneOffset.UTC)).withNano(0);
        LocalDateTime startTime = getDateTime(series, currentTime);
        URI uri = new URIBuilder(FXRATESAPI_BASE_URL)
                .appendPath(TIMESERIES)
                .addParameter("start_date", startTime.toString())
                .addParameter("end_date", currentTime.toString())
                .addParameter("accuracy", accuracy(series))
                .build();
        HttpGet get = new HttpGet(uri);
        CloseableHttpResponse httpResponse = httpClient.httpClient().execute(get);
        HistoricalSeriesDataResponse response = new HistoricalSeriesDataResponse();
        if (httpResponse.getCode() == 200) {
            String responseEntity = EntityUtils.toString(httpResponse.getEntity());
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            HistoricalSeriesDataVendorResponse vendorResponse = mapper.readValue(responseEntity, HistoricalSeriesDataVendorResponse.class);
            List<RateDateSeries> rateDateSeries = new ArrayList<>();
            for(Map.Entry<Date, Map<String, Double>> rateDate : vendorResponse.getRates().entrySet()) {
                RateDateSeries dateRate = new RateDateSeries();
                dateRate.setDateTime(dateToLocalDateTime(rateDate.getKey()));
                Map<String, Double> rates = rateDate.getValue();
                BigDecimal rate = doubleToBigDecimal(rates.get(toCurrency));
                dateRate.setRate(rate);
                rateDateSeries.add(dateRate);
            }
            buildHistoricalDataSeries(response, fromCurrency, toCurrency, startTime, currentTime, series, rateDateSeries);
        }
        return response;
    }

    private void buildHistoricalDataSeries(HistoricalSeriesDataResponse response, String fromCurrency, String toCurrency, LocalDateTime startTime, LocalDateTime currentTime, String series, List<RateDateSeries> rateDateSeries) throws URISyntaxException, IOException, ParseException {
        response.setFromCurrency(fromCurrency);
        response.setToCurrency(toCurrency);
        response.setFromTime(startTime);
        response.setToTime(currentTime);
        response.setRange(series);
        response.setRateDateSeries(rateDateSeries);
        ConvertLatestDto convertLatestDto = new ConvertLatestDto();
        convertLatestDto.setFromCurrency(fromCurrency);
        convertLatestDto.setToCurrency(toCurrency);
        convertLatestDto.setAmount(BigDecimal.valueOf(1));
        CurrencyConverterResponse converterResponse = currencyConverter(convertLatestDto);
        response.setCurrencyConverter(converterResponse);
        response.setClose(converterResponse.getRate());
        List<BigDecimal> ratesList = new ArrayList<>();
        for(RateDateSeries rate : rateDateSeries) {
            ratesList.add(rate.getRate());
        }
        BigDecimal min = Collections.min(ratesList);
        BigDecimal max = Collections.max(ratesList);
        response.setLow(min);
        response.setHigh(max);
        response.setPercentageChange(findPercentage(max, min));
    }

    private LocalDateTime getDateTime(String series, LocalDateTime currentTime) {
        LocalDateTime startTime = null;
        switch (series) {
            case "12H" -> startTime = currentTime.minusMinutes(720);
            case "1D" -> startTime = currentTime.minusHours(24);
            case "1W" -> startTime = currentTime.minusHours(167);
            case "1M" -> startTime = currentTime.minusMonths(1);
            case "3M" -> startTime = currentTime.minusMonths(3);
            case "6M" -> startTime = currentTime.minusMonths(6);
            case "1Y" -> startTime = currentTime.minusDays(365);
            default -> throw new IllegalStateException("Unexpected value: " + series);
        }
        return startTime;
    }

    private String accuracy(String series) {
        String setAccuracy;
        switch (series) {
            case "12H" -> setAccuracy = "5m";
            case "1D" -> setAccuracy = "15m";
            case "1W" -> setAccuracy = "hour";
            case "1M", "6M", "3M", "1Y" -> setAccuracy = "day";
            default -> throw new IllegalStateException("Unexpected value: " + series);
        }
        return setAccuracy;
    }
}