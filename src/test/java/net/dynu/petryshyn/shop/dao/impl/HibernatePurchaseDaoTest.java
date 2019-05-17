package net.dynu.petryshyn.shop.dao.impl;

import net.dynu.petryshyn.shop.dao.PurchaseDao;
import net.dynu.petryshyn.shop.bean.Purchase;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class HibernatePurchaseDaoTest {

    private static SessionFactory sessionFactory;
    private static PurchaseDao purchaseDao;

    private Purchase USDPurchase;
    private Purchase UAHPurchase;
    private Purchase OldPurchase;

    @BeforeAll
    static void setUp() {
        ApplicationContext context = new ClassPathXmlApplicationContext("context/TestApplicationContext.xml");
        sessionFactory = context.getBean("sessionFactory", SessionFactory.class);
        purchaseDao = context.getBean("purchaseDao", PurchaseDao.class);
    }

    @BeforeEach
    void prepareData(){
        //Creating purchases with different currencies
        USDPurchase = new Purchase();
        USDPurchase.setName("addTest123123");
        USDPurchase.setCurrency(Currency.getInstance("USD"));
        USDPurchase.setDate(LocalDate.now());
        USDPurchase.setPrise(new BigDecimal("150.50"));

        USDPurchase.setId(purchaseDao.add(USDPurchase));

        UAHPurchase = new Purchase();
        UAHPurchase.setName("addTest123123");
        UAHPurchase.setCurrency(Currency.getInstance("UAH"));
        UAHPurchase.setDate(LocalDate.now());
        UAHPurchase.setPrise(new BigDecimal("2450.00"));

        UAHPurchase.setId(purchaseDao.add(UAHPurchase));

        //Creating purchase from another year
        OldPurchase = new Purchase();
        OldPurchase.setName("addTest123123");
        OldPurchase.setCurrency(Currency.getInstance("UAH"));
        OldPurchase.setDate(LocalDate.of(1999, 12, 12));
        OldPurchase.setPrise(new BigDecimal("2450.00"));

        OldPurchase.setId(purchaseDao.add(OldPurchase));
    }

    @AfterEach
    //Returning database to empty state
    void rollBack(){
        Session session = sessionFactory.getCurrentSession();

        session.beginTransaction();
        Query query = session.createQuery("delete from Purchase");
        query.executeUpdate();
        session.getTransaction().commit();
    }

    @Test
    void addPurchaseTest() {
        //Creating unique purchase in order to be sure that later we will getByDate exactly this purchase
        Purchase addedPurchase = new Purchase();
        addedPurchase.setName("addTest123123");
        addedPurchase.setCurrency(Currency.getInstance("USD"));
        addedPurchase.setDate(LocalDate.now());
        addedPurchase.setPrise(new BigDecimal("150.50"));

        long addedPurchaseId = purchaseDao.add(addedPurchase);

        //Getting recently added purchase
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        Purchase actual = session.get(Purchase.class, addedPurchaseId);
        session.getTransaction().commit();

        assertEquals(addedPurchase, actual);
    }

    @Test
    void getPurchaseByIdTest() {
        //Testing getByDate(long id) method
        Purchase actual = purchaseDao.getById(USDPurchase.getId());

        //Checking results
        assertEquals(USDPurchase, actual);
    }

    @Test
    void getPurchaseByDateTest() {
        List<Purchase>expected = Arrays.asList(USDPurchase, UAHPurchase);

        //Getting purchases with getByDate(LocalDate date) method
        List<Purchase> actual = purchaseDao.getByDate(LocalDate.now());

        //Checking results
        assertEquals(expected, actual);
    }

    @Test
    void deletePurchaseByIdTest() {
        //Deleting test purchase
        purchaseDao.deleteById(USDPurchase.getId());

        //Trying to find deleted purchase
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        Purchase deletedPurchase = session.get(Purchase.class, USDPurchase.getId());
        Purchase existingOldPurchase = session.get(Purchase.class, OldPurchase.getId());
        session.getTransaction().commit();

        //Checking is the purchase was really deleted
        assertNull(deletedPurchase);

        //But OldPurchase must exist
        assertNotNull(existingOldPurchase);
    }

    @Test
    void deletePurchaseByDateTest() {
        //Deleting today's purchases
        purchaseDao.deleteByDate(LocalDate.now());

        //Trying to find today's purchases
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        Purchase deletedUSDPurchase = session.get(Purchase.class, USDPurchase.getId());
        Purchase deletedUAHPurchase = session.get(Purchase.class, UAHPurchase.getId());
        Purchase existingOldPurchase = session.get(Purchase.class, OldPurchase.getId());
        session.getTransaction().commit();

        //USD and UAH purchases must be deleted
        assertNull(deletedUAHPurchase);
        assertNull(deletedUSDPurchase);

        //OldPurchase must exist
        assertNotNull(existingOldPurchase);

    }

    @Test
    void getAllPurchasesSortedByDateTest() {
        //Getting getAllSortedByDate() method result
        List<Purchase> actual = purchaseDao.getAllSortedByDate();

        //Expecting result
        List<Purchase> expected = Arrays.asList(OldPurchase, USDPurchase, UAHPurchase);

        //Checking results
        assertEquals(expected, actual);
    }

    @Test
    void getPurchaseByYearTest() {
        //Preparing expected result
        List<Purchase> expected = Arrays.asList(USDPurchase, UAHPurchase);

        List<Purchase> actual = purchaseDao.getByYear(LocalDate.now().getYear());

        //Checking results
        assertEquals(expected, actual);

        //Result of this call must be empty list
        actual = purchaseDao.getByYear(22);

        assertTrue(actual.isEmpty());
    }

    @Test
    void getCurrenciesReportTest() {
        Map<Currency, BigDecimal> expected = new HashMap<>();
        expected.put(USDPurchase.getCurrency(), USDPurchase.getPrise());
        expected.put(UAHPurchase.getCurrency(), UAHPurchase.getPrise());

        Map<Currency, BigDecimal> actual = purchaseDao.getCurrenciesReport(LocalDate.now().getYear());

        assertEquals(expected, actual);
    }
}