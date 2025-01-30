package bank.com.app;
import bank.com.config.DatabaseSingleton;
import bank.com.core.EntityManager;
import bank.com.server.ServerManager;
import bank.com.service.AccountService;
import bank.com.service.TransactionService;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        Connection connection = null;
        try {
            connection = DatabaseSingleton.getInstance();
            System.out.println("Database connection established successfully.");
        } catch (Exception e) {
            System.err.println("Failed to initialize the database connection. Please restart the application and try again. :D");

            System.exit(1);
        }
        EntityManager entityManager = new EntityManager(connection);
        AccountService accountService = new AccountService(entityManager);
        TransactionService transactionService = new TransactionService(entityManager);
        ServerManager serverManager = new ServerManager(65525, 5, accountService, transactionService);
        serverManager.startServer();
    }
}