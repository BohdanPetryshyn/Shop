package net.dynu.petryshyn.shop.shell.command;


import com.budhash.cliche.Command;
import com.budhash.cliche.InputConverter;
import com.budhash.cliche.Param;
import net.dynu.petryshyn.shop.dao.PurchaseDao;
import net.dynu.petryshyn.shop.bean.Purchase;
import net.dynu.petryshyn.shop.shell.converter.ToBigDecimalConverter;
import net.dynu.petryshyn.shop.shell.converter.ToCurrencyConverter;
import net.dynu.petryshyn.shop.shell.converter.ToLocalDateConverter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

public class Add {

    private PurchaseDao purchaseDao;
    private All AllCommand;

    public Add(PurchaseDao purchaseDao, All AllCommand) {
        this.purchaseDao = purchaseDao;
        this.AllCommand = AllCommand;
    }

    @Command(description = "adds purchases made by customers\n" +
            " in any supported currency (e.g. USD, EUR, PLN, etc.)\n" +
            " to the list of purchases. Purchases for various dates\n" +
            " could be added in any order.")
    public String purchase(
            @Param(name="date", description="The date when purchase has occurred")
                    LocalDate date,
            @Param(name="price", description="An amount of money spent by customer")
                    BigDecimal price,
            @Param(name="currency", description="The currency in which purchase has occurred")
                    Currency currency,
            @Param(name="name", description="The name of the product purchased")
                    String name){

        Purchase newPurchase = new Purchase();
        newPurchase.setDate(date);
        newPurchase.setPrise(price);
        newPurchase.setCurrency(currency);
        newPurchase.setName(name);

        StringBuilder output = new StringBuilder();

        try {

            purchaseDao.add(newPurchase);
            output.append(AllCommand.all());

        } catch (Exception ex){
            output.append("Filed to add purchase.").append("\n")
                    .append("Error: ").append(ex.getMessage());
        }

        return output.toString();
    }
}
