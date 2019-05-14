package net.dynu.petryshyn.shop.shell.command;

import net.dynu.petryshyn.shop.currency.CurrencyConverter;
import net.dynu.petryshyn.shop.dao.PurchaseDao;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReportTest {

    @Test
    void report() throws IOException {
        //Mocking required constructor arguments for normal conditions test
        PurchaseDao purchaseDaoMock = mock(PurchaseDao.class);

        int year = 2019;

        Map<Currency, BigDecimal> testCurrenciesReport = new HashMap<>();
        testCurrenciesReport.put(Currency.getInstance("USD"), BigDecimal.ZERO);
        testCurrenciesReport.put(Currency.getInstance("UAH"), BigDecimal.TEN);

        when(purchaseDaoMock.getCurrenciesReport(year)).thenReturn(testCurrenciesReport);

        CurrencyConverter converterMock = mock(CurrencyConverter.class);

        BigDecimal testConvertResult = BigDecimal.TEN;

        when(converterMock.convert(testCurrenciesReport, Currency.getInstance("UAH")))
                .thenReturn(testConvertResult);

        //Testing report command
        Report reportCommand = new Report(purchaseDaoMock, converterMock);

        String result = reportCommand.report(2019, Currency.getInstance("UAH"));

        assertTrue(result.contains("10"));
        assertTrue(result.contains("UAH"));

        //Mocking required constructor arguments for extreme conditions test
        String errorMessage = "test error";

        when(purchaseDaoMock.getCurrenciesReport(2019)).thenThrow(new RuntimeException(errorMessage));

        //Testing report command in extreme conditions
        result = reportCommand.report(2019, Currency.getInstance("UAH"));

        assertTrue(result.contains(errorMessage));
    }
}