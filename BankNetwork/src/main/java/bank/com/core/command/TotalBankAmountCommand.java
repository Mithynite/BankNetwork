package bank.com.core.command;

import bank.com.service.AccountService;

import java.io.PrintWriter;

/**
 * This class represents a command for calculating and printing the total bank amount.
 * It implements the Command interface and is responsible for executing the command.
 * @author Jakub Hofman
 */
public class TotalBankAmountCommand implements Command {
    private final AccountService accountService;
    private final PrintWriter writer;

    public TotalBankAmountCommand(AccountService accountService, PrintWriter writer) {
        this.accountService = accountService;
        this.writer = writer;
    }

    @Override
    public void execute(String[] args) {
        try {
            long totalAmount = accountService.getAllAccounts().stream().mapToLong(a -> a.getBalance()).sum();
            writer.println("BA " + totalAmount);
        } catch (Exception e) {
            writer.println("ER Chyba při získávání celkové částky: " + e.getMessage());
        }
    }
}
