package net.dynu.petryshyn.shop.shell.converter;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;

class ToCurrencyConverterTest {

    @Test
    void convertInput() throws Exception {
        ToCurrencyConverter converter = new ToCurrencyConverter();

        //Normal conditions test
        Currency expected = Currency.getInstance("UAH");

        Currency actual = (Currency)converter.convertInput("UAH", Currency.class);

        assertEquals(expected, actual);

        //Extreme conditions test
        assertThrows(Exception.class, () -> converter.convertInput("UAss", Currency.class));

        //This isn't a responsibility of current converter
        actual = (Currency)converter.convertInput("UAH", BigDecimal.class);

        assertNull(actual);
    }
}