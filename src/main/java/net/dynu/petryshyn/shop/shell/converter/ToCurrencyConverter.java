package net.dynu.petryshyn.shop.shell.converter;

import com.budhash.cliche.InputConverter;

import java.util.Currency;

public class ToCurrencyConverter implements InputConverter {
    @Override
    public Object convertInput(String s, Class<?> toClass) throws Exception {
        if(toClass.equals(Currency.class)){//If this is responsibility of current converter
            return Currency.getInstance(s);
        } else {
            return null;
        }
    }
}
