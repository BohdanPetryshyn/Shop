package net.dynu.petryshyn.shop.currency;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;

public class CurrencyConverter {
    private CurrencyRatesProvider ratesProvider;

    public CurrencyConverter(CurrencyRatesProvider ratesProvider) {
        this.ratesProvider = ratesProvider;
    }

    public BigDecimal convert(Currency from, Currency to, BigDecimal amount) throws IOException {
        BigDecimal rate = ratesProvider.rate(from, to);
        return amount.multiply(rate);
    }

    public BigDecimal convert(Map<Currency, BigDecimal> currenciesAmount, Currency to) throws IOException {
        BigDecimal result = BigDecimal.ZERO;
        BigDecimal rate;

        for(Map.Entry<Currency, BigDecimal> currencyAmount : currenciesAmount.entrySet()){
            rate = ratesProvider.rate(currencyAmount.getKey(), to);
            result = result.add(currencyAmount.getValue().multiply(rate));
        }

        return result;
    }
}
