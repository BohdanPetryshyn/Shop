package net.dynu.petryshyn.shop.currency.impl;

import net.dynu.petryshyn.shop.currency.CurrencyRatesProvider;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;

class FixerIoCurrencyRatesProviderTest {

    @Test
    void rateTest() throws IOException, InterruptedException {
        MockWebServer server = new MockWebServer();

        String USD = "1.123086";
        String UAH = "29.400175";
        String RUB = "73.438067";

        String API_KEY = "123456";
        String FAVORITE_CURRENCIES = "USD,UAH,RUB,EUR";

        String ETAG = "testETag";
        String DATE = "testDate";

        //Preparing server responses
        //First with normal 200 response code
        server.enqueue(new MockResponse()
                .setBody(
                "{" +
                        "\"success\":true," +
                        "\"timestamp\":1557748324," +
                        "\"base\":\"EUR\"," +
                        "\"date\":\"2019-05-13\"," +
                        "\"rates\":{" +
                                    "\"USD\":" + USD + "," +
                                    "\"UAH\":" + UAH + "," +
                                    "\"RUB\":" + RUB + "," +
                                    "\"EUR\":1}" +
                "}")
                .setHeader("ETag", ETAG)
                .setHeader("Date", DATE));
        //And another with response code 304 not modified
        server.enqueue(new MockResponse().setResponseCode(304));

        server.start();

        HttpUrl baseUrl = server.url("/api");

        CurrencyRatesProvider ratesProvider = new FixerIoCurrencyRatesProvider(baseUrl.toString(),
                API_KEY,
                10,
                FAVORITE_CURRENCIES);

        //First rate method invocation must cache rates
        Currency base = Currency.getInstance("USD");
        Currency target = Currency.getInstance("UAH");
        BigDecimal rate = ratesProvider.rate(base, target);

        BigDecimal expectedResult = new BigDecimal(UAH).divide(new BigDecimal(USD), 6, RoundingMode.DOWN);

        assertEquals(expectedResult, rate);

        //And the second method invocation must use cached rates
        // if the server returned response code 304
        base = Currency.getInstance("UAH");
        target = Currency.getInstance("RUB");
        rate = ratesProvider.rate(base, target);

       expectedResult = new BigDecimal(RUB).divide(new BigDecimal(UAH), 6, RoundingMode.DOWN);

        assertEquals(expectedResult, rate);

        //Checking Etag and Date header
        //Calling takeRequest() two times to getByDate second request
        server.takeRequest();
        RecordedRequest recordedRequest = server.takeRequest();

        //Checking request parameters given to the server
        assertEquals(ETAG, recordedRequest.getHeader("If-None-Match"));
        assertEquals(DATE, recordedRequest.getHeader("If-Modified-Since"));
        
        server.shutdown();
    }


    @Test
    void FixerIoRequestFailureTest() throws IOException {
        String FIXER_ERROR_MESSAGE = "Your monthly API request volume has been reached. Please upgrade your plan.";

        MockWebServer server = new MockWebServer();

        server.enqueue(new MockResponse().setBody(
                "{" +
                "  \"success\": false," +
                "  \"error\": {" +
                "    \"code\": 104," +
                "    \"info\": \"" + FIXER_ERROR_MESSAGE + "\"    " +
                "  }" +
                "}"));

        server.start();

        HttpUrl url = server.url("/api");



        CurrencyRatesProvider ratesProvider = new FixerIoCurrencyRatesProvider(url.toString(),
                "testKey",
                10,
                "UAH,USD");

        //Calling rate() method and expecting exception with specified message;
        try {
            ratesProvider.rate(Currency.getInstance("UAH"), Currency.getInstance("USD"));
        } catch (IOException ex){
            assertTrue(ex.getMessage().contains(FIXER_ERROR_MESSAGE));
            return;
        }
        fail("Exception was expected but haven't been thrown thrown");
    }
}



