package net.dynu.petryshyn.shop.shell.command;

import com.budhash.cliche.Command;
import com.budhash.cliche.Param;
import net.dynu.petryshyn.shop.dao.PurchaseDao;

import java.time.LocalDate;

public class Clear {

    private PurchaseDao purchaseDao;
    private All allCommand;

    public Clear(PurchaseDao purchaseDao, All allCommand) {
        this.purchaseDao = purchaseDao;
        this.allCommand = allCommand;
    }

    @Command(description = "Removes all purchases for specified date")
    public String clear(
            @Param(name = "date", description = "the date for which all purchases should be removed")
                    LocalDate date){

        StringBuilder output = new StringBuilder();

        try{

            purchaseDao.deleteByDate(date);
            output.append(allCommand.all());

        } catch (Exception ex){
            output.append("Filed to clear purchase.").append("\n")
                    .append("Error: ").append(ex.getMessage());
        }

        return output.toString();
    }
}
