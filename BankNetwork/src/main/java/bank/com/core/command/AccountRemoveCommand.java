package bank.com.core.command;

import bank.com.model.Account;
import bank.com.model.Transaction;
import bank.com.model.TransactionType;
import bank.com.service.AccountService;
import bank.com.service.TransactionService;

import java.io.PrintWriter;
import java.util.Date;

/**
 * This class represents a command for removing an account from the banking system.
 * It implements the Command interface and provides functionality to execute the command.
 * @author Jakub Hofman
 */
public class AccountRemoveCommand implements Command {
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final PrintWriter writer;
    private final String bankCode;

    /**
     * Constructs a new instance of AccountRemoveCommand.
     *
     * @param accountService the service for managing accounts
     * @param transactionService the service for managing transactions
     * @param writer the output writer for sending messages to the user
     * @param bankCode the code of the bank
     */
    public AccountRemoveCommand(AccountService accountService, TransactionService transactionService, PrintWriter writer, String bankCode) {
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.writer = writer;
        this.bankCode = bankCode;
    }

    /**
     * Executes the command to remove an account from the banking system.
     *
     * @param args the command-line arguments
     */
    @Override
    public void execute(String[] args) {
        if (args.length != 2) {
            writer.println("ER Formát čísla účtu není správný.");
            return;
        }

        try {
            Account account = findAccountByFormattedNumber(args[1]);
            if (account == null) {
                writer.println("ER Bankovní účet neexistuje.");
                return;
            }

            if (account.getBalance() != 0) {
                writer.println("ER Nelze smazat bankovní účet na kterém jsou finance.");
                return;
            }

            writer.println("AR");
            accountService.removeAccount(account.getAccountId());
            transactionService.createTransaction(new Transaction(
                    account.getAccountNumber(), bankCode, TransactionType.remove, new Date()));
        } catch (Exception e) {
            writer.println("ER Chyba při mazání účtu!");
        }
    }

    /**
     * Finds an account by its formatted number.
     *
     * @param accountInfo the formatted account number
     * @return the found account or null if not found
     */
    private Account findAccountByFormattedNumber(String accountInfo) {
        String[] parts = accountInfo.split("/");
        if (parts.length != 2 || !parts[1].equals(bankCode)) {
            return null;
        }

        try {
            int accountNumber = Integer.parseInt(parts[0]);
            return accountService.findAccountByNumber(accountNumber);
        } catch (Exception e) {
            writer.println("ER Neplatné číslo účtu.");
            return null;
        }
    }
}
