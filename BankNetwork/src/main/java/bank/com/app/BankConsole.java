package bank.com.app;

import bank.com.config.ConfigManager;
import bank.com.core.command.*;
import bank.com.service.AccountService;
import bank.com.service.TransactionService;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.*;
/**
* Class serving the command options and responses.
* @author Jakub Hofman
*/
public class BankConsole {
    private final PrintWriter writer;
    private final BufferedReader reader;
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final String bankCode;
    private final Map<String, Command> commandMap = new HashMap<>();

    // Configuration values
    private static final int COMMAND_TIMEOUT = ConfigManager.commandTimeout * 1000; // Time in seconds
    private static final int MIN_ACCOUNT_NUMBER = ConfigManager.accountNumberMin;
    private static final int MAX_ACCOUNT_NUMBER = ConfigManager.accountNumberMax;
    private static final int MIN_ACCOUNT_BALANCE = ConfigManager.accountBalanceMin;

    private static final int PORT_MINIMUM = ConfigManager.portMinimum;
    private static final int PORT_MAXIMUM = ConfigManager.portMaximum;


    public BankConsole(PrintWriter writer, BufferedReader reader, AccountService accountService, TransactionService transactionService, String bankCode) {
        this.writer = writer;
        this.reader = reader;
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.bankCode = bankCode;
        registerCommands();
    }

    private void registerCommands() {
        commandMap.put("AC", new AccountCreateCommand(accountService, transactionService, writer, bankCode, MIN_ACCOUNT_NUMBER, MAX_ACCOUNT_NUMBER, MIN_ACCOUNT_BALANCE));
        commandMap.put("AR", new AccountRemoveCommand(accountService, transactionService, writer, bankCode));
        commandMap.put("AD", new AccountDepositCommand(accountService, transactionService, writer, bankCode, PORT_MINIMUM, PORT_MAXIMUM));
        commandMap.put("AW", new AccountWithdrawCommand(accountService, transactionService, writer, bankCode, PORT_MINIMUM, PORT_MAXIMUM));
        commandMap.put("AB", new AccountBalanceCommand(accountService, writer, bankCode, PORT_MINIMUM, PORT_MAXIMUM));
        commandMap.put("BN", new BankNumberOfClientsCommand(accountService, writer));
        commandMap.put("BC", new BankCodeCommand(writer, bankCode));
        commandMap.put("BA", new TotalBankAmountCommand(accountService, writer));

        commandMap.put("AL", new ListAllAccountsCommand(accountService, writer)); // My own additional Command (Prints out all the accounts of the Bank)
    }

        /**
     * Starts the bank console application and processes user input.
     * This method continuously reads user input, executes commands, and runs until the user enters "EXIT".
     * 
     * The method performs the following steps:
     * 1. Creates a Scanner to read input from the BufferedReader.
     * 2. Enters a loop that continues until the user enters "EXIT".
     * 3. Flushes the writer to ensure all output is sent.
     * 4. Reads a line of input, trims whitespace, and converts it to uppercase.
     * 5. If the input is "EXIT", prints a goodbye message and exits the loop.
     * 6. Otherwise, splits the input into arguments and executes the corresponding command.
     */
    public void start() {
        Scanner scanner = new Scanner(reader);
        while (true) {
            writer.flush();
            String input = scanner.nextLine().trim().toUpperCase();

            if ("EXIT".equals(input)) {
                writer.println("Goodbye!");
                break;
            }
            executeCommand(input.split(" "));
        }
    }

        /**
     * Executes a command based on the provided arguments.
     * This method validates the input, retrieves the appropriate command,
     * and executes it in a separate thread with a timeout mechanism.
     *
     * @param args An array of Strings representing the command and its arguments.
     *             The first element (args[0]) is expected to be the command name.
     *             Subsequent elements are treated as arguments for the command.
     *
     * @return void This method doesn't return a value, but it writes output to the console
     *              using the 'writer' object. Possible outputs include:
     *              - Error messages for invalid or unknown commands
     *              - Results of successful command execution
     *              - Error messages for exceptions during command execution
     *              - Timeout message if the command execution exceeds the allowed time
     */
    private void executeCommand(String[] args) {
        if (args.length == 0) {
            writer.println("ER Invalid command!");
            return;
        }

        Command command = commandMap.get(args[0]);
        if (command == null) {
            writer.println("ER Invalid command!");
            return;
        }

        Thread commandThread = new Thread(() -> {
            try {
                command.execute(args);
            } catch (Exception e) {
                writer.println("ER Something went wrong while executing the command!");
            }
        });

        commandThread.start(); // Start command execution

        try {
            commandThread.join(COMMAND_TIMEOUT);
            if (commandThread.isAlive()) {
                commandThread.interrupt(); // Stop the thread if still running
                writer.println("ER Command took too long to execute, sorry!");
            }
        } catch (InterruptedException e) {
            writer.println("ER Unexpected error occurred: " + e.getMessage());
        }
    }
}
