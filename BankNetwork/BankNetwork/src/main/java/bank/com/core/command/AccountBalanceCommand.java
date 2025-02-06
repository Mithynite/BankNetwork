package bank.com.core.command;

import bank.com.model.Account;
import bank.com.service.AccountService;

import java.io.PrintWriter;

/**
 * Represents a command to retrieve the balance of a bank account.
 * This command can handle both local and remote account balance requests.
 * @author Jakub Hofman
 */
public class AccountBalanceCommand extends RemoteCommand {

    /**
     * Constructs a new AccountBalanceCommand.
     *
     * @param accountService The service used to interact with account data.
     * @param writer The PrintWriter used to output results.
     * @param bankCode The code identifying the current bank.
     * @param portStart The starting port number for remote communication.
     * @param portEnd The ending port number for remote communication.
     */
    public AccountBalanceCommand(AccountService accountService, PrintWriter writer, String bankCode, int portStart, int portEnd) {
        super(accountService, writer, bankCode, portStart, portEnd);
    }

    /**
     * Executes the account balance command.
     * This method retrieves the balance of a specified account, either from the local bank
     * or by forwarding the request to a remote bank if necessary.
     *
     * @param args An array of String arguments. Expected format:
     *             args[0] - account number
     *             args[1] - target bank IP
     * @throws Exception If there's an error during execution, particularly during parsing or remote communication.
     */
    @Override
    public void execute(String[] args) throws Exception {
        String[] parts = splitBankAccountArguments(args, 2);
        if (parts == null) return;

        int accountNumber = Integer.parseInt(parts[0]);
        String targetBankIP = parts[1];

        if (!targetBankIP.equals(bankCode)) {
            writer.println(forwardRequestToRemoteBank(targetBankIP, String.join(" ", args)));
            return;
        }

        Account account = accountService.findAccountByNumber(accountNumber);
        if (account == null) {
            writer.println("ER Bankovní účet neexistuje.");
            return;
        }

        writer.println("AB " + account.getBalance());
    }
}
