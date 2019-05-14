package net.dynu.petryshyn.shop.shell.command;

import net.dynu.petryshyn.shop.dao.PurchaseDao;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClearTest {

    @Test
    //In this test we expect only that PurchaseDao.deleteByDate() will be invoked
    void clear() {

        //Mocking required constructor arguments for normal conditions test
        PurchaseDao purchaseDaoMock = mock(PurchaseDao.class);

        All allCommandMock = mock(All.class);

        when(allCommandMock.all()).thenReturn("");

        Clear clearCommand = new Clear(purchaseDaoMock, allCommandMock);

        clearCommand.clear(LocalDate.now());

        verify(purchaseDaoMock, times(1)).deleteByDate(LocalDate.now());

        //Mocking required constructor arguments for normal conditions test
        String errorMessage = "test error";

        doThrow(new RuntimeException(errorMessage))
                .when(purchaseDaoMock)
                .deleteByDate(LocalDate.now());

        String result = clearCommand.clear(LocalDate.now());

        assertTrue(result.contains(errorMessage));
    }
}