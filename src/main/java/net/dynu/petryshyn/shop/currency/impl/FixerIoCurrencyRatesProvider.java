package net.dynu.petryshyn.shop.currency.impl;

import com.google.gson.Gson;
import net.dynu.petryshyn.shop.currency.CurrencyRatesProvider;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

public class FixerIoCurrencyRatesProvider implements CurrencyRatesProvider {

    private final String BASE = "EUR";

    //This currencies will be requested every time
    private StringBuilder favoriteCurrencies;
    private OkHttpClient httpClient;
    //USD - based cache (all rates are usd-based)
    private Map<Currency, BigDecimal> lastRates;
    private String lastETagValue;
    private String lastRatesReceivingTime;
    //Pre-initialized UrlBuilder static part
    private HttpUrl.Builder urlBuilder;
    //JSON to Object converter
    private Gson gson;

    public FixerIoCurrencyRatesProvider(String fixerApiUrl, String accessKey, String favoriteCurrencies) {
        this.favoriteCurrencies = new StringBuilder(favoriteCurrencies);
        httpClient = new OkHttpClient();
        lastRates = new HashMap<>();
        lastETagValue = "";
        lastRatesReceivingTime = "";
        //Initializing static part of urlBuilder
        urlBuilder = HttpUrl.parse(fixerApiUrl + "/latest").newBuilder();
        urlBuilder.addQueryParameter("access_key", accessKey);
        urlBuilder.addQueryParameter("base", BASE);
        gson = new Gson();
    }

    @Override
    public BigDecimal rate(Currency base, Currency target) throws IOException {
        BigDecimal result;
        String baseCode = base.getCurrencyCode();
        String targetCode = target.getCurrencyCode();
        //Appending new currencies if they not in favorites yet
        if(favoriteCurrencies.indexOf(baseCode) < 0){
            favoriteCurrencies.append(",").append(baseCode);
        }
        if(favoriteCurrencies.indexOf(targetCode) < 0){
            favoriteCurrencies.append(",").append(targetCode);
        }
        urlBuilder.addQueryParameter("symbols", favoriteCurrencies.toString());

        Request.Builder requestBuilder = new Request.Builder()
                .url(urlBuilder.build().toString())
                .get()
                .addHeader("Accept", "application/json; Charset=UTF-8");
        if(lastETagValue != null && lastRatesReceivingTime != null){
            requestBuilder
                    .addHeader("If-None-Match", lastETagValue)
                    .addHeader("If-Modified-Since", lastRatesReceivingTime);
        }
        Request request = requestBuilder.build();
        try(Response response = httpClient.newCall(request).execute()){
            if(response.code() == 200){
                lastETagValue = response.header("ETag");
                lastRatesReceivingTime = response.header("Date");
                FixerLatestResponse fixerResponse = gson.fromJson(response.body().string(), FixerLatestResponse.class);
                handleFixerResponse(fixerResponse);
            } else if(response.code() != 304){
                throw new IOException("Unexpected networking error. " + response);
            }
        }
        return lastRates.get(target).divide(lastRates.get(base), 6, RoundingMode.DOWN);
    }

    private void handleFixerResponse(FixerLatestResponse response) throws IOException {
        if(response.success){
            //Updating rates
            lastRates.clear();
            for(Map.Entry<String, String> rate : response.rates.entrySet()){
                BigDecimal newRation = new BigDecimal(rate.getValue());
                Currency newCurrency = Currency.getInstance(rate.getKey());
                lastRates.put(newCurrency, newRation);
            }
        } else {
            String errorCode = response.error.get("code");
            String errorInfo = response.error.get("info");
            throw new IOException("Filed to getByDate rates from Fixer.io. Error: " + errorCode + ": " + errorInfo);
        }
    }


    private class FixerLatestResponse {
        private boolean success;
        private long timestamp;
        private String base;
        private String date;
        private Map<String, String> rates;
        private Map<String, String> error;

    }
}
