package bank.com.core.command;

import bank.com.model.Account;
import bank.com.service.AccountService;

import java.io.PrintWriter;
import java.util.List;

/**
 * This class represents a command to list all accounts in the system.
 * It implements the Command interface and provides an execute method to perform the command.
 * @author Jakub Hofman
 */
public class ListAllAccountsCommand implements Command {
    private final AccountService accountService;
    private final PrintWriter writer;

    public ListAllAccountsCommand(AccountService accountService, PrintWriter writer) {
        this.accountService = accountService;
        this.writer = writer;
    }

    @Override
    public void execute(String[] args) {
        try {
            List<Account> accounts = accountService.getAllAccounts();
            writer.println("AL");
            for (Account account : accounts) {
                writer.println(account);
            }
        } catch (Exception e) {
            writer.println("ER Listing all accounts failed!");
        }
    }
}
