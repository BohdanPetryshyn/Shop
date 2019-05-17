package net.dynu.petryshyn.shop.shell.command;

import net.dynu.petryshyn.shop.bean.Purchase;
import net.dynu.petryshyn.shop.dao.PurchaseDao;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AllTest {

    @Test
    void allCommandTest() {
        //Mocking required constructor arguments for normal conditions test
        PurchaseDao purchaseDaoMock = mock(PurchaseDao.class);

        Purchase testPurchase1 = new Purchase();
        testPurchase1.setName("test111");
        testPurchase1.setCurrency(Currency.getInstance("USD"));
        testPurchase1.setDate(LocalDate.of(2019, 3, 20));
        testPurchase1.setPrise(new BigDecimal("40"));

        Purchase testPurchase2 = new Purchase();
        testPurchase2.setName("test222");
        testPurchase2.setCurrency(Currency.getInstance("UAH"));
        testPurchase2.setDate(LocalDate.of(2019, 11, 25));
        testPurchase2.setPrise(new BigDecimal("40"));

        List<Purchase> testDaoResult = Arrays.asList(testPurchase1, testPurchase2);

        when(purchaseDaoMock.getAllSortedByDate()).thenReturn(testDaoResult);

        All allCommand = new All(purchaseDaoMock);

        String result = allCommand.all();

        //Applying result
        assertTrue(result.indexOf("test111") < result.indexOf("test222"));
        assertTrue(result.contains("USD"));
        assertTrue(result.contains("40"));
        assertTrue(result.contains("2019-11-25"));

        //Mocking required constructor arguments for extreme conditions test
        String errorMessage = "test error";

        when(purchaseDaoMock.getAllSortedByDate()).thenThrow(new RuntimeException(errorMessage));

        //Testing extreme conditions
        result = allCommand.all();

        assertTrue(result.contains(errorMessage));
    }
}