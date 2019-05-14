package net.dynu.petryshyn.shop.bean;

import org.junit.jupiter.api.Test;
import org.meanbean.lang.Factory;
import org.meanbean.test.*;

import java.time.LocalDate;
import java.util.Currency;

class PurchaseTest {
    @Test
    void gettersAndSettersShouldFunctionCorrectly() {
        BeanTester beanTester = new BeanTester();
        beanTester.getFactoryCollection().addFactory(LocalDate.class, new LocalDateFactory());
        beanTester.getFactoryCollection().addFactory(Currency.class, new CurrencyFactory());
        beanTester.testBean(Purchase.class);
    }

    @Test
    void equalsShouldFunctionCorrectly() {
        EqualsMethodTester tester = new EqualsMethodTester();
        tester.getFactoryCollection().addFactory(LocalDate.class, new LocalDateFactory());
        tester.getFactoryCollection().addFactory(Currency.class, new CurrencyFactory());
        Configuration configuration = new ConfigurationBuilder()
                .ignoreProperty("id")
                .build();
        tester.testEqualsMethod(Purchase.class, configuration);
    }

    @Test
    void hashCodeShouldFunctionCorrectly() {
        HashCodeMethodTester tester = new HashCodeMethodTester();
        tester.getFactoryCollection().addFactory(LocalDate.class, new LocalDateFactory());
        tester.getFactoryCollection().addFactory(Currency.class, new CurrencyFactory());
        tester.testHashCodeMethod(Purchase.class);
    }

    private class LocalDateFactory implements Factory<LocalDate>{

        @Override
        public LocalDate create() {
            return LocalDate.now();
        }
    }

    private class CurrencyFactory implements Factory<Currency>{

        @Override
        public Currency create() {
            return Currency.getInstance("USD");
        }
    }
}