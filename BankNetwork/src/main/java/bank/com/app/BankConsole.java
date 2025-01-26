package bank.com.app;

import bank.com.Command;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class BankConsole {
    private final PrintWriter writer;
    private final BufferedReader reader;
    private final Map<String, Command> commandMap = new HashMap<>();

    /**
     * Constructor to initialize the commands.
     */
    public BankConsole(PrintWriter writer, BufferedReader reader) {
        this.writer = writer;
        this.reader = reader;
        initializeCommands();
    }

    /**
     * Initialize the commands and register them into the `commandMap`.
     */
    private void initializeCommands() {
        try {
            // Dynamically register all commands and their implementations
            registerCommand("AC", this.getClass().getDeclaredMethod("createAccount", Scanner.class));
            registerCommand("AD", this.getClass().getDeclaredMethod("deleteAccount", Scanner.class));
            registerCommand("DP", this.getClass().getDeclaredMethod("deposit", Scanner.class));
            registerCommand("WD", this.getClass().getDeclaredMethod("withdraw", Scanner.class));
            registerCommand("BA", this.getClass().getDeclaredMethod("balanceInquiry", Scanner.class));
            registerCommand("TL", this.getClass().getDeclaredMethod("transactionLog", Scanner.class));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Failed to register commands", e);
        }
    }

    /**
     * Register a command into the command map.
     *
     * @param code   The command key.
     * @param method The method to be executed for this command.
     */
    private void registerCommand(String code, Method method) {
        commandMap.put(code, new Command(code, method, this));
    }

    /**
     * Start the command interaction loop.
     */
    public void start() {
        writer.println("Welcome to the Bank Console!");
        Scanner scanner = new Scanner(reader); // Use the `reader` to interact with a client

        while (true) {
            writer.print("\nEnter command: ");
            writer.flush(); // Ensure output is sent to the client
            String commandKey = scanner.nextLine().trim().toUpperCase();

            Command command = commandMap.get(commandKey);

            if (command != null) {
                try {
                    command.execute(scanner); // Execute the command with the current scanner
                } catch (Exception e) {
                    writer.println("Error executing command: " + e.getMessage());
                }
            } else if ("EXIT".equals(commandKey)) {
                writer.println("Goodbye!");
                break;
            } else {
                writer.println("Invalid command! Try again.");
            }
        }
    }

    // Command implementations
    public void createAccount(Scanner scanner) {
        writer.print("Enter account number: ");
        writer.flush();
        int accountNumber = scanner.nextInt();
        writer.print("Enter initial balance: ");
        writer.flush();
        long initialBalance = scanner.nextLong();
        scanner.nextLine(); // Consume the newline character

        // TODO: Add logic to create an account
        writer.println("Account created: " + accountNumber);
    }

    public void deleteAccount(Scanner scanner) {
        writer.print("Enter account number to delete: ");
        writer.flush();
        int accountNumber = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // TODO: Add logic to delete an account
        writer.println("Account deleted: " + accountNumber);
    }

    public void deposit(Scanner scanner) {
        writer.print("Enter account number: ");
        writer.flush();
        int accountNumber = scanner.nextInt();
        writer.print("Enter deposit amount: ");
        writer.flush();
        long amount = scanner.nextLong();
        scanner.nextLine(); // Consume newline

        // TODO: Add logic to deposit money
        writer.println("Deposit successful.");
    }

    public void withdraw(Scanner scanner) {
        writer.print("Enter account number: ");
        writer.flush();
        int accountNumber = scanner.nextInt();
        writer.print("Enter withdrawal amount: ");
        writer.flush();
        long amount = scanner.nextLong();
        scanner.nextLine(); // Consume newline

        // TODO: Add logic to withdraw money
        writer.println("Withdrawal successful.");
    }

    public void balanceInquiry(Scanner scanner) {
        writer.print("Enter account number: ");
        writer.flush();
        int accountNumber = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // TODO: Add logic for balance inquiry
        writer.println("Account balance: 1000");
    }

    public void transactionLog(Scanner scanner) {
        writer.print("Enter account number to view transactions: ");
        writer.flush();
        int accountNumber = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // TODO: Add logic to fetch the transaction log
        writer.println("Transaction log: ...");
    }
}
