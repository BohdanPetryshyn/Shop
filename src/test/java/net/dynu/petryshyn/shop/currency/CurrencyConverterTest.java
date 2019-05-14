package net.dynu.petryshyn.shop.currency;

import net.dynu.petryshyn.shop.currency.impl.FixerIoCurrencyRatesProvider;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CurrencyConverterTest {

    @Test
    void convertTest() throws IOException {

        Currency base = Currency.getInstance("USD");
        Currency target = Currency.getInstance("UAH");
        BigDecimal rate = new BigDecimal("29.400");
        BigDecimal amount = new BigDecimal("2");

        //Configuring mock to replace real CurrencieRatesProvider
        CurrencyRatesProvider ratesProvider = mock(FixerIoCurrencyRatesProvider.class);
        when(ratesProvider.rate(base, target)).thenReturn(rate);

        CurrencyConverter converter = new CurrencyConverter(ratesProvider);

        BigDecimal result = converter.convert(base, target, amount);

        BigDecimal expectedResult = amount.multiply(rate);

        assertEquals(expectedResult, result);
    }
}