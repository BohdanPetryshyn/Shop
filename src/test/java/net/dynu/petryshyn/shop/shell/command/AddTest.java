package net.dynu.petryshyn.shop.shell.command;

import net.dynu.petryshyn.shop.bean.Purchase;
import net.dynu.petryshyn.shop.dao.PurchaseDao;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddTest {

    @Test
    void purchaseTest() {
        //Mocking required constructor arguments for normal conditions test
        PurchaseDao purchaseDaoMock = mock(PurchaseDao.class);

        All allCommandMock = mock(All.class);

        String showAllReply = "Test reply";

        when(allCommandMock.all()).thenReturn(showAllReply);

        Add addCommand = new Add(purchaseDaoMock, allCommandMock);

        String result = addCommand.purchase(
                LocalDate.now(),
                new BigDecimal("123"),
                Currency.getInstance("UAH"),
                "testPurchase");

        assertEquals(showAllReply, result);

        //Mocking required constructor arguments for extreme conditions test
        String errorMessage = "test error";
        Purchase testPurchase = new Purchase();

        when(purchaseDaoMock.add(testPurchase)).thenThrow(new RuntimeException(errorMessage));

        result = addCommand.purchase(null, null, null, null);

        assertTrue(result.contains(errorMessage));
    }
}