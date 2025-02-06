package bank.com.core.command;

import bank.com.model.Account;
import bank.com.model.Transaction;
import bank.com.model.TransactionType;
import bank.com.service.AccountService;
import bank.com.service.TransactionService;

import java.io.PrintWriter;
import java.util.Date;
import java.util.Random;

/**
 * This class represents a command for creating a new bank account.
 * It implements the Command interface and provides functionality to execute the command.
 * @author Jakub Hofman
 */
public class AccountCreateCommand implements Command {
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final PrintWriter writer;
    private final String bankCode;
    private final int minAccountNumber;
    private final int maxAccountNumber;
    private final int minAccountBalance;

    /**
     * Constructor for AccountCreateCommand.
     *
     * @param accountService      The service for managing bank accounts.
     * @param transactionService The service for managing transactions.
     * @param writer             The PrintWriter for outputting messages.
     * @param bankCode           The bank code for the new account.
     * @param minAccountNumber   The minimum account number.
     * @param maxAccountNumber   The maximum account number.
     * @param minAccountBalance  The minimum initial account balance.
     */
    public AccountCreateCommand(AccountService accountService, TransactionService transactionService, PrintWriter writer, String bankCode, int minAccountNumber, int maxAccountNumber, int minAccountBalance) {
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.writer = writer;
        this.bankCode = bankCode;
        this.minAccountNumber = minAccountNumber;
        this.maxAccountNumber = maxAccountNumber;
        this.minAccountBalance = minAccountBalance;
    }

    /**
     * Executes the command to create a new bank account.
     *
     * @param args Command-line arguments (not used in this implementation).
     */
    @Override
    public void execute(String[] args) {
        try {
            int accountNumber = generateAccountNumber();
            Account account = new Account(accountNumber, minAccountBalance);
            accountService.createAccount(account);
            writer.println("AC " + accountNumber + "/" + bankCode);
            transactionService.createTransaction(new Transaction(
                    accountNumber, bankCode, TransactionType.create, new Date()));
        } catch (Exception e) {
            writer.println("ER Bank nyní bohužel neumožňuje tvorbu nových účtů! Zkuste to znovu později.");
        }
    }

    /**
     * Generates a random account number within the specified range.
     *
     * @return The generated account number.
     */
    private int generateAccountNumber() {
        return new Random().nextInt(maxAccountNumber - minAccountNumber) + minAccountNumber;
    }
}

