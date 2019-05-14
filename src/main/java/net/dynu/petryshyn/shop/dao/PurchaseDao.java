package net.dynu.petryshyn.shop.dao;

import net.dynu.petryshyn.shop.bean.Purchase;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;
import java.util.Map;

public interface PurchaseDao {

    long add(Purchase purchase);

    Purchase getById(long id);
    List<Purchase> getByDate(LocalDate date);
    List<Purchase> getByYear(int year);
    List<Purchase> getAllSortedByDate();
    Map<Currency, BigDecimal> getCurrenciesReport(int year);


    void deleteById(long id);
    void deleteByDate(LocalDate date);
}
