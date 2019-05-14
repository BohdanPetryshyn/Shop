package net.dynu.petryshyn.shop.dao.impl;

import net.dynu.petryshyn.shop.dao.PurchaseDao;
import net.dynu.petryshyn.shop.bean.Purchase;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HibernatePurchaseDao implements PurchaseDao {

    private SessionFactory sessionFactory;

    private final String GET_BY_DATE_HQL             = "from Purchase " +
                                                       "where date = :date";


    private final String GET_ALL_SORTED_BY_DATE_HQL  = "from Purchase " +
                                                       "order by date";


    private final String DELETE_BY_ID_HQL            = "delete " +
                                                       "from Purchase " +
                                                       "where id = :id";


    private final String DELETE_BY_DATE_HQL          = "delete " +
                                                       "from Purchase " +
                                                       "where date = :date";


    private final String GET_BY_YEAR_HQL             = "from Purchase " +
                                                       "where year(date) = :year";


    private final String GET_CURRENCIES_REPORT       = "select sum(prise), currency " +
                                                       "from Purchase " +
                                                       "where year(date) = :year " +
                                                       "group by currency";

    public HibernatePurchaseDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public long add(Purchase purchase) {
        long id;
        Transaction transaction = null;
        Session session = sessionFactory.getCurrentSession();

        try{
            transaction = session.beginTransaction();
            id = (long)session.save(purchase);
            transaction.commit();
        }
        catch (Exception ex){
            if(transaction != null){
                transaction.rollback();
            }
            throw ex;
        }
        return id;
    }

    @Override
    public Purchase getById(long id) {
        Purchase purchase;
        Transaction transaction = null;
        Session session = sessionFactory.getCurrentSession();

        try{
            transaction = session.beginTransaction();
            purchase = session.get(Purchase.class, id);
            transaction.commit();
        }catch (Exception ex){
            if(transaction != null){
                transaction.rollback();
            }
            throw ex;
        }

        return purchase;
    }

    @Override
    public List<Purchase> getByDate(LocalDate date) {
        List<Purchase> purchases;
        Transaction transaction = null;
        Session session = sessionFactory.getCurrentSession();

        try{
            transaction = session.beginTransaction();
            Query<Purchase> query = session.createQuery(GET_BY_DATE_HQL, Purchase.class);
            query.setParameter("date", date);
            purchases = query.list();
            transaction.commit();
        }catch (Exception ex){
            if (transaction != null){
                transaction.rollback();
            }
            throw ex;
        }
        return  purchases;
    }

    @Override
    public List<Purchase> getByYear(int year) {
        List<Purchase> purchases;
        Transaction transaction = null;
        Session session = sessionFactory.getCurrentSession();

        try{
            transaction = session.beginTransaction();
            Query<Purchase> query = session.createQuery(GET_BY_YEAR_HQL, Purchase.class);
            query.setParameter("year", year);
            purchases = query.list();
            transaction.commit();
        }catch (Exception ex){
            if (transaction != null){
                transaction.rollback();
            }
            throw ex;
        }
        return  purchases;
    }

    @Override
    public List<Purchase> getAllSortedByDate() {
        List<Purchase> purchases;
        Transaction transaction = null;
        Session session = sessionFactory.getCurrentSession();

        try{
            transaction = session.beginTransaction();
            Query<Purchase> query = session.createQuery(GET_ALL_SORTED_BY_DATE_HQL, Purchase.class);
            purchases = query.list();
            transaction.commit();
        }catch (Exception ex){
            if(transaction != null){
                transaction.rollback();
            }
            throw ex;
        }
        return purchases;
    }

    @Override
    public Map<Currency, BigDecimal> getCurrenciesReport(int year) {
        Map<Currency, BigDecimal> currenciesReport = new HashMap<>();
        Transaction transaction = null;
        Session session = sessionFactory.getCurrentSession();

        try{
            transaction = session.beginTransaction();
            Query<Object[]> query = session.createQuery(GET_CURRENCIES_REPORT, Object[].class);
            query.setParameter("year", year);
            List<Object[]> results = query.list();
            transaction.commit();
            for(Object[] result : results){
                currenciesReport.put((Currency) result[1], (BigDecimal) result[0]);
            }
        }catch (Exception ex){
            if(transaction != null){
                transaction.rollback();
            }
            throw ex;
        }
        return currenciesReport;
    }

    @Override
    public void deleteById(long id) {
        Transaction transaction = null;
        Session session = sessionFactory.getCurrentSession();

        try{
            transaction = session.beginTransaction();
            Query query = session.createQuery(DELETE_BY_ID_HQL);
            query.setParameter("id", id);
            query.executeUpdate();
            transaction.commit();
        }catch (Exception ex){
            if(transaction != null){
                transaction.rollback();
            }
            throw ex;
        }
    }

    @Override
    public void deleteByDate(LocalDate date) {
        Transaction transaction = null;
        Session session = sessionFactory.getCurrentSession();

        try{
            transaction = session.beginTransaction();
            Query query = session.createQuery(DELETE_BY_DATE_HQL);
            query.setParameter("date", date);
            query.executeUpdate();
            transaction.commit();
        }catch (Exception ex){
            if(transaction != null){
                transaction.rollback();
            }
            throw ex;
        }
    }


}
