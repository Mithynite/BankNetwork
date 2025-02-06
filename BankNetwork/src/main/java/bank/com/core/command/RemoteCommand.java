package bank.com.core.command;

import bank.com.service.AccountService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * This class represents a remote command that interacts with a remote bank.
 * It provides methods for splitting bank account arguments, parsing amounts,
 * and forwarding requests to the remote bank.
 * @author Jakub Hofman
 */
public class RemoteCommand implements Command {
    protected final AccountService accountService;
    protected final PrintWriter writer;
    protected final String bankCode;
    protected final int portStart;
    protected final int portEnd;

    /**
     * Constructs a new RemoteCommand instance.
     *
     * @param accountService the account service to be used for operations
     * @param writer the PrintWriter to write responses to
     * @param bankCode the code of the remote bank
     * @param portStart the starting port number for connecting to the remote bank
     * @param portEnd the ending port number for connecting to the remote bank
     */
    public RemoteCommand(AccountService accountService, PrintWriter writer, String bankCode, int portStart, int portEnd) {
        this.accountService = accountService;
        this.writer = writer;
        this.bankCode = bankCode;
        this.portStart = portStart;
        this.portEnd = portEnd;
    }

    /**
     * Splits the bank account arguments into bank code and account number.
     *
     * @param args the command-line arguments
     * @param expectedLength the expected length of the arguments array
     * @return an array containing the bank code and account number, or null if the arguments are invalid
     */
    protected String[] splitBankAccountArguments(String[] args, int expectedLength) {
        if (args.length != expectedLength) {
            writer.println("ER Formát čísla účtu není správný.");
            return null;
        }

        String[] parts = args[1].split("/");
        if (parts.length != 2) {
            writer.println("ER Neplatný formát účtu.");
            return null;
        }

        return parts;
    }

    /**
     * Parses the given amount string into a long value.
     *
     * @param amountStr the amount string to be parsed
     * @return the parsed amount, or null if the string is not a valid amount
     */
    protected Long parseAmount(String amountStr) {
        try {
            long amount = Long.parseLong(amountStr);
            if (amount <= 0) {
                writer.println("ER Částka musí být větší než 0.");
                return null;
            }
            return amount;
        } catch (NumberFormatException e) {
            writer.println("ER Neplatná částka.");
            return null;
        }
    }

    /**
     * Forwards the given command to the remote bank using a socket connection.
     *
     * @param remoteBankCode the code of the remote bank
     * @param command the command to be forwarded
     * @return the response from the remote bank, or an error message if the connection fails
     */
    protected String forwardRequestToRemoteBank(String remoteBankCode, String command) {
        for (int port = portStart; port <= portEnd; port++) {
            try (Socket socket = new Socket(remoteBankCode, port);
                 PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                out.println(command);
                return in.readLine();
            } catch (Exception e) {
                System.out.println("Trying next port: " + (port + 1));
            }
        }
        return "ER Nelze se připojit k bance " + bankCode + " na žádném dostupném portu.";
    }

    /**
     * Executes the command with the given arguments.
     *
     * @param args the command-line arguments
     * @throws Exception if an error occurs during command execution
     */
    @Override
    public void execute(String[] args) throws Exception {}
}
