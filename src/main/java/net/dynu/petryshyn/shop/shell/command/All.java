package net.dynu.petryshyn.shop.shell.command;

import com.budhash.cliche.Command;
import net.dynu.petryshyn.shop.bean.Purchase;
import net.dynu.petryshyn.shop.dao.PurchaseDao;

import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

public class All {

    private PurchaseDao purchaseDao;

    public All(PurchaseDao purchaseDao) {
        this.purchaseDao = purchaseDao;
    }

    @Command(description = "Shows all purchases sorted by date")
    public String all(){

        StringBuilder output = new StringBuilder();

        try {

            List<Purchase> purchases = purchaseDao.getAllSortedByDate();
            output.append(prepareOutput(purchases));

        } catch (Exception ex){
            output.append("Filed to get purchases.").append("\n")
                    .append("Error: ").append(ex.getMessage());
        }

        return output.toString();
    }

    private String prepareOutput(List<Purchase> purchases){
        StringBuilder output = new StringBuilder();

        //Preventing index out of bounds exception
        if(purchases.isEmpty()){
            return "";
        }

        LocalDate lastDate = purchases.get(0).getDate();
        output.append(lastDate).append("\n");

        for(Purchase purchase : purchases){
            if(!purchase.getDate().equals(lastDate)){
                output.append("\n")
                        .append(purchase.getDate()).append("\n");
                lastDate = purchase.getDate();
            }
            output.append(purchase.getName()).append(" ")
                    .append(purchase.getPrise()).append(" ")
                    .append(purchase.getCurrency()).append("\n");
        }
        return output.toString();
    }
}
