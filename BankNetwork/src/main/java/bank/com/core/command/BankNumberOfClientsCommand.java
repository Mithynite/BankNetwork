package bank.com.core.command;

import bank.com.service.AccountService;

import java.io.PrintWriter;

/**
 * This class represents a command for retrieving the number of clients in the bank.
 * It implements the Command interface and sends the number of clients to the output writer.
 * @author Jakub Hofman
 */
public class BankNumberOfClientsCommand implements Command {
    private final AccountService accountService;
    private final PrintWriter writer;

    public BankNumberOfClientsCommand(AccountService accountService, PrintWriter writer) {
        this.accountService = accountService;
        this.writer = writer;
    }

    @Override
    public void execute(String[] args) {
        try {
            writer.println("BN " + accountService.getAllAccounts().size());
        } catch (Exception e) {
            writer.println("ER Error during account retrieval!");
        }
    }
}
