package bank.com.core.command;

import bank.com.service.AccountService;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
            writer.println("ER The format of the account number is not correct!");
            return null;
        }

        String[] parts = args[1].split("/");
        if (parts.length != 2) {
            writer.println("ER The format of the account is not correct!");
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
                writer.println("ER The amount must be greater than 0!");
                return null;
            }
            return amount;
        } catch (NumberFormatException e) {
            writer.println("ER Invalid amount!");
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
        int numThreads = Math.min(10, portEnd - portStart + 1); // Limit threads to avoid too many connections
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        List<Future<String>> futures = new ArrayList<>();

        for (int port = portStart; port <= portEnd; port++) {
            final int currentPort = port; // Needed because lambda expressions require final or effectively final variables
            futures.add(executor.submit(() -> tryConnect(remoteBankCode, currentPort, command)));
        }

        executor.shutdown(); // Prevents new tasks from being submitted

        try {
            for (Future<String> future : futures) {
                String response = future.get(); // Wait for each task to complete
                if (response != null) { // If a valid response is received, return it
                    return response;
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return "ER Sorry, we could not connect to the bank: " + bankCode + " on any defined port.";
    }

    /**
     * Tries to establish a connection to the remote bank on a given port.
     * @return the response from the remote bank, or null if the connection fails.
     */
    private String tryConnect(String remoteBankCode, int port, String command) {
        try (Socket socket = new Socket(remoteBankCode, port);
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println(command);
            return in.readLine(); // Return the response
        } catch (IOException e) {
            return null; // If connection fails, return null
        }
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
