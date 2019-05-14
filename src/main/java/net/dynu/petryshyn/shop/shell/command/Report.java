package net.dynu.petryshyn.shop.shell.command;

import com.budhash.cliche.Command;
import com.budhash.cliche.Param;
import net.dynu.petryshyn.shop.currency.CurrencyConverter;
import net.dynu.petryshyn.shop.dao.PurchaseDao;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;

public class Report {
    private PurchaseDao purchaseDao;
    private CurrencyConverter currencyConverter;

    public Report(PurchaseDao purchaseDao, CurrencyConverter currencyConverter) {
        this.purchaseDao = purchaseDao;
        this.currencyConverter = currencyConverter;
    }

    @Command(description = "This command should take a list of cross-currency exchange\n" +
            " rates from http://fixer.io (register for a free plan), calculate\n" +
            " the total income for specified year, convert and present it in\n" +
            " specified currency")
    public String report(
            @Param(name = "year", description = "year for which total income should be calculated")
                    int year,
            @Param(name = "currency", description = "currency in which total income is presented")
                    Currency targetCurrency){

        StringBuilder output = new StringBuilder();

        try{

            Map<Currency, BigDecimal> currenciesAmount = purchaseDao.getCurrenciesReport(year);
            BigDecimal report = currencyConverter.convert(currenciesAmount, targetCurrency);
            output.append(report).append(" ").append(targetCurrency);

        } catch (Exception ex){
            output.append("Filed to build report.").append("\n")
                    .append("Error: ").append(ex.getMessage());
        }

        return output.toString();
    }
}
