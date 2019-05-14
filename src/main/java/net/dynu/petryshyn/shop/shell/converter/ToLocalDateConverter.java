package net.dynu.petryshyn.shop.shell.converter;

import com.budhash.cliche.InputConverter;

import java.time.LocalDate;

public class ToLocalDateConverter implements InputConverter {

    @Override
    public Object convertInput(String s, Class<?> toClass) throws Exception {
        if(toClass.equals(LocalDate.class)){//If this is responsibility of current converter
            return LocalDate.parse(s);
        } else {
            return null;
        }
    }
}
