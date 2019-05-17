package net.dynu.petryshyn.shop.currency;

import net.dynu.petryshyn.shop.currency.impl.FixerIoCurrencyRatesProvider;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CurrencyConverterTest {

    @Test
    void singleConversionTest() throws IOException {

        Currency base = Currency.getInstance("USD");
        Currency target = Currency.getInstance("UAH");
        BigDecimal rate = new BigDecimal("29.400");
        BigDecimal amount = new BigDecimal("2");

        //Configuring mock to replace real CurrencyRatesProvider
        CurrencyRatesProvider ratesProvider = mock(CurrencyRatesProvider.class);
        when(ratesProvider.rate(base, target)).thenReturn(rate);

        CurrencyConverter converter = new CurrencyConverter(ratesProvider);

        BigDecimal result = converter.convert(base, target, amount);

        BigDecimal expectedResult = amount.multiply(rate);

        assertEquals(expectedResult, result);
    }

    @Test
    void multipleConversionTest() throws IOException {

        //Preparing test input
        Map<Currency, BigDecimal> currenciesAmount = new HashMap<>();

        Currency currency1 = Currency.getInstance("USD");
        Currency currency2 = Currency.getInstance("UAH");
        Currency targetCurrency = Currency.getInstance("EUR");

        BigDecimal rate1 = new BigDecimal("0.9");
        BigDecimal rate2 = new BigDecimal("31.2");

        BigDecimal amount1 = BigDecimal.ONE;
        BigDecimal amount2 = BigDecimal.TEN;

        currenciesAmount.put(currency1, amount1);
        currenciesAmount.put(currency2, amount2);

        //Mocking CurrencyRatesProvider
        CurrencyRatesProvider ratesProvider = mock(CurrencyRatesProvider.class);
        when(ratesProvider.rate(currency1, targetCurrency)).thenReturn(rate1);
        when(ratesProvider.rate(currency2, targetCurrency)).thenReturn(rate2);

        CurrencyConverter converter = new CurrencyConverter(ratesProvider);

        //Expected result
        BigDecimal expected = amount1.multiply(rate1).add(amount2.multiply(rate2));

        BigDecimal actual = converter.convert(currenciesAmount, targetCurrency);

        //Checking results
        assertEquals(expected, actual);
    }
}