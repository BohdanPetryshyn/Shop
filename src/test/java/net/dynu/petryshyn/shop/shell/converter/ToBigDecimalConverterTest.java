package net.dynu.petryshyn.shop.shell.converter;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;

class ToBigDecimalConverterTest {
    @Test
    void ConvertInputTest() throws Exception {
        ToBigDecimalConverter converter = new ToBigDecimalConverter();

        //Normal conditions test
        BigDecimal expected = new BigDecimal("123.2");

        BigDecimal actual = (BigDecimal) converter.convertInput("123.2", BigDecimal.class);

        assertEquals(expected, actual);

        //Extreme conditions test
        assertThrows(NumberFormatException.class, () -> converter.convertInput("uab", BigDecimal.class));

        //This isn't a responsibility of current converter
        actual = (BigDecimal) converter.convertInput("213", Currency.class);

        assertNull(actual);
    }
}