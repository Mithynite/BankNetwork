package bank.com.server;

import bank.com.app.BankConsole;
import bank.com.config.ConfigManager;
import bank.com.service.AccountService;
import bank.com.service.TransactionService;

import java.io.*;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Handles communication with a single client.
 * Manages client connection, timeout, and console interaction.
 * @author Jakub Hofman
 */
public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final String bankCode;
    private PrintWriter writer;
    private BufferedReader reader;
    private long lastActivityTime;
    private static final long TIMEOUT_MILLIS = ConfigManager.clientTimeout * 1000L;
    private Timer timeoutTimer;

    /**
     * Constructs a new ClientHandler instance.
     *
     * @param clientSocket The socket representing the client connection.
     * @param accountService The service for managing accounts.
     * @param transactionService The service for managing transactions.
     * @param bankCode The code of the bank associated with the client.
     */
    public ClientHandler(Socket clientSocket, AccountService accountService, TransactionService transactionService, String bankCode) {
        this.clientSocket = clientSocket;
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.bankCode = bankCode;
        this.lastActivityTime = System.currentTimeMillis(); // Save the time when the Client connected
    }

    @Override
    public void run() {
        try {
            writer = new PrintWriter(clientSocket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // Start timeout checker
            startTimeoutChecker();

            // Create and start console interaction
            BankConsole console = new BankConsole(writer, reader, accountService, transactionService, bankCode);
            console.start();

        } catch (Exception e) {
            System.err.println("Error communicating with client: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    /**
     * Starts a timer that checks for inactivity and disconnects the client if they exceed the timeout.
     */
    private void startTimeoutChecker() {
        timeoutTimer = new Timer(true);
        timeoutTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (System.currentTimeMillis() - lastActivityTime > TIMEOUT_MILLIS) {
                    writer.println("ER Byl jste odhlášen kvůli nečinnosti.");
                    closeConnection();
                }
            }
        }, TIMEOUT_MILLIS, TIMEOUT_MILLIS);
    }

    /**
     * Resets the activity timer when the user interacts.
     */
    private void resetActivityTimer() {
        lastActivityTime = System.currentTimeMillis();
    }

    /**
     * Reads input from the client and resets the activity timer.
     *
     * @return The input read from the client, or null if the client disconnected.
     */
    public String readLine() {
        try {
            String input = reader.readLine();
            if (input != null) {
                resetActivityTimer(); // Reset timeout on activity
            }
            return input;
        } catch (IOException e) {
            System.err.println("Error reading input from client: " + e.getMessage());
            return null; // Treat as client disconnection
        }
    }

    /**
     * Closes the client connection gracefully.
     */
    private void closeConnection() {
        try {
            if (timeoutTimer != null) timeoutTimer.cancel();
            if (writer != null) writer.close();
            if (reader != null) reader.close();
            clientSocket.close();
            System.out.println("Client disconnected:" + clientSocket);
        } catch (IOException e) {
            System.err.println("Error closing client socket: " + e.getMessage());
        }
    }
}
