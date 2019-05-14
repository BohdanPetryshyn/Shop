package net.dynu.petryshyn.shop.currency;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Currency;

public interface CurrencyRatesProvider {
    BigDecimal rate(Currency base, Currency target) throws IOException;
}
