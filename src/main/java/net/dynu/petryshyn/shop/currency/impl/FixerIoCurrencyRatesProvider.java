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
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class FixerIoCurrencyRatesProvider implements CurrencyRatesProvider {

    //Last rates cache
    private Map<Currency, BigDecimal> cache = new HashMap<>();

    //Currencies that will be requested in every fixer.io request
    private Set<Currency> cachedCurrencies;

    //Last cache update time
    private LocalDateTime lastUpdate = LocalDateTime.MIN;

    //Last response headers
    private String lastETagValue = "";
    private String lastDateValue = "";

    //Cache life time in minutes
    private final int CACHE_LIFE_TIME;

    //Http client
    private OkHttpClient client = new OkHttpClient();

    //Url builder which contains static parameters
    private HttpUrl.Builder urlBuilder;

    //Json-object converter
    private Gson gson = new Gson();

    public FixerIoCurrencyRatesProvider(String apiUrl, String accessKey, int CACHE_LIFE_TIME, String favoriteCurrencies) {
        this.CACHE_LIFE_TIME = CACHE_LIFE_TIME;

        cachedCurrencies = new HashSet<>();
        //Converting string to set
        String[] currencies = favoriteCurrencies.split("\\s*,\\s*");
        for(String currency : currencies){
            cachedCurrencies.add(Currency.getInstance(currency));
        }

        //Static part of urlBuilder
        urlBuilder = HttpUrl.parse(apiUrl + "/latest").newBuilder();
        urlBuilder.addQueryParameter("access_key", accessKey);
        urlBuilder.addQueryParameter("base", "EUR");

    }

    @Override
    public BigDecimal rate(Currency base, Currency target) throws IOException {

        //Adding base and target currencies if they aren't already there
        cachedCurrencies.add(base);
        cachedCurrencies.add(target);

        //Updating cache
        if(!isCacheUpToDate()){
            updateCache();
        }

        return getRatefromCache(base, target);
    }

    private void updateCache() throws IOException {
        //Converting List to String to put it to the url as the required currencies
        String currencies = cachedCurrencies
                .stream()
                .map(Currency::getCurrencyCode)
                .collect(Collectors.joining(","));

        //Building request url
        String url = urlBuilder
                .setQueryParameter("symbols", currencies)
                .build()
                .toString();

        FixerLatestResponse fixerResponse = getFixerResponse(url);

        if(fixerResponse == null){      //If rates haven't been changed
            lastUpdate = LocalDateTime.now();
        } else {                        //If rates have been changed
            if(fixerResponse.success){
                //Updating cache
                cache.clear();
                for(Map.Entry<String, String> rate : fixerResponse.rates.entrySet()){
                    BigDecimal newRation = new BigDecimal(rate.getValue());
                    Currency newCurrency = Currency.getInstance(rate.getKey());
                    cache.put(newCurrency, newRation);
                }
                lastUpdate = LocalDateTime.now();
            } else {
                String errorCode = fixerResponse.error.get("code");
                String errorInfo = fixerResponse.error.get("info");
                throw new IOException("Filed to getByDate rates from Fixer.io. Error: " + errorCode + ": " + errorInfo);
            }
        }
    }

    //Return null if rates haven't been changed on fixer.io
    private FixerLatestResponse getFixerResponse(String url) throws IOException {
        //Building request
        Request request = new Request.Builder()
                .url(url)
                .addHeader("If-None-Match", lastETagValue)
                .addHeader("If-Modified-Since", lastDateValue)
                .build();

        try(Response response = client.newCall(request).execute()){

            if(response.code() == 200){             //If request succeed

                //Updating received headers
                lastETagValue = response.header("ETag");
                lastDateValue = response.header("Date");

                String responseJson = response.body().string();

                //Converting json to object
                return gson.fromJson(responseJson, FixerLatestResponse.class);
            } else if(response.code() == 304){      //If rates haven't been changed on fixer.io

                return null;

            } else {                                //If request failed

                throw new IOException("Unexpected networking error. " + response);

            }
        }
    }

    private BigDecimal getRatefromCache(Currency base, Currency target){
        return cache.get(target).divide(cache.get(base), 6, RoundingMode.DOWN);
    }

    private boolean isCacheUpToDate(){
        LocalDateTime now = LocalDateTime.now();

        return lastUpdate.compareTo(now.minusMinutes(CACHE_LIFE_TIME)) > 0  //Cache has been updated recently
                && cache.keySet().containsAll(cachedCurrencies);            //Cache contains all necessary currencies
    }

    //This class represents fixer.io response
    private class FixerLatestResponse {
        private boolean success;
        private long timestamp;
        private String base;
        private String date;
        private Map<String, String> rates;
        private Map<String, String> error;

    }
}
