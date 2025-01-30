package bank.com.service;

import bank.com.app.BankConsole;

import java.io.*;
import java.net.Socket;

public class ClientHandler {
    private final Socket clientSocket;
    private AccountService accountService;
    private TransactionService transactionService;
    private String bankCode;

    public ClientHandler(Socket clientSocket, AccountService accountService, TransactionService transactionService, String bankCode) {
        this.clientSocket = clientSocket;
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.bankCode = bankCode;
    }

    public void handleClient() {
        try (
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
        ) {
            BankConsole console = new BankConsole(writer, reader, accountService, transactionService, bankCode);
            console.start(); // The main logic for interacting with the client
        } catch (Exception e) {
            System.err.println("Error communicating with client: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing client socket: " + e.getMessage());
            }
        }
    }
}
