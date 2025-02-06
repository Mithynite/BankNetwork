package bank.com.core.command;

import bank.com.model.Account;
import bank.com.model.Transaction;
import bank.com.model.TransactionType;
import bank.com.service.AccountService;
import bank.com.service.TransactionService;

import java.io.PrintWriter;
import java.util.Date;

/**
 * This class represents a command for depositing money into an account.
 * It extends the RemoteCommand class and overrides the execute method.
 *
 * @author Jakub Hofman
 */
public class AccountDepositCommand extends RemoteCommand {
    private final TransactionService transactionService;

    /**
     * Constructs a new instance of AccountDepositCommand.
     *
     * @param accountService    the account service for managing accounts
     * @param transactionService the transaction service for managing transactions
     * @param writer            the PrintWriter for sending responses to the client
     * @param bankCode          the bank code of the current bank
     * @param portStart         the starting port number for forwarding requests
     * @param portEnd           the ending port number for forwarding requests
     */
    public AccountDepositCommand(AccountService accountService, TransactionService transactionService, PrintWriter writer, String bankCode, int portStart, int portEnd) {
        super(accountService, writer, bankCode, portStart, portEnd);
        this.transactionService = transactionService;
    }

    /**
     * Executes the deposit command.
     *
     * @param args the command-line arguments
     * @throws Exception if an error occurs during execution
     */
    @Override
    public void execute(String[] args) throws Exception {
        String[] parts = splitBankAccountArguments(args, 3);
        if (parts == null) return;

        int accountNumber = Integer.parseInt(parts[0]);
        String targetBankIP = parts[1];

        Long amount = parseAmount(args[2]);
        if (amount == null) return;

        if (!targetBankIP.equals(bankCode)) {
            writer.println(forwardRequestToRemoteBank(targetBankIP, String.join(" ", args)));
            return;
        }

        Account account = accountService.findAccountByNumber(accountNumber);
        if (account == null) {
            writer.println("ER Bankovní účet neexistuje.");
            return;
        }

        account.deposit(amount);
        accountService.updateAccount(account);

        transactionService.createTransaction(new Transaction(accountNumber, bankCode, TransactionType.deposit, amount, new Date()));
        writer.println("AD");
    }
}
