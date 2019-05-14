package net.dynu.petryshyn.shop.shell.converter;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.*;

class ToLocalDateConverterTest {

    @Test
    void convertInput() throws Exception {
        ToLocalDateConverter converter = new ToLocalDateConverter();

        //Normal conditions test
        LocalDate expected = LocalDate.of(2019, 12, 25);

        LocalDate actual = (LocalDate)converter.convertInput("2019-12-25", LocalDate.class);

        assertEquals(expected, actual);

        //Extreme conditions test
        assertThrows(DateTimeParseException.class, () -> converter.convertInput("2019-12-33", LocalDate.class));

        //This isn't a responsibility of current converter
        actual = (LocalDate)converter.convertInput("2019-12-25", BigDecimal.class);

        assertNull(actual);
    }
}