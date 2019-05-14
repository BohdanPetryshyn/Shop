package net.dynu.petryshyn.shop.shell.converter;

import com.budhash.cliche.InputConverter;

import java.math.BigDecimal;

public class ToBigDecimalConverter implements InputConverter {

    @Override
    public Object convertInput(String s, Class<?> toClass) throws Exception {
        if(toClass.equals(BigDecimal.class)){//If this conversion is responsibility of current converter
            return new BigDecimal(s);
        } else {
            return null;
        }
    }
}
