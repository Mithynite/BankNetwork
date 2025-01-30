package bank.com.app;

import bank.com.Command;
import bank.com.model.Account;
import bank.com.model.Transaction;
import bank.com.service.AccountService;
import bank.com.service.TransactionService;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.*;

public class BankConsole {
    private final PrintWriter writer;
    private final BufferedReader reader;
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final String bankCode;
    private final List<Command> commands = new ArrayList<>();
    private final Map<String, Command> commandMap = new HashMap<>();

    public BankConsole(PrintWriter writer, BufferedReader reader, AccountService accountService, TransactionService transactionService, String bankCode) {
        this.writer = writer;
        this.reader = reader;
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.bankCode = bankCode;
        registerCommands();
    }

    private void registerCommands() {
        addCommand(new Command("AC", this::createAccount));
        addCommand(new Command("AR", this::removeAccount));
        addCommand(new Command("AD", this::deposit));
        addCommand(new Command("AW", this::withdraw));
        addCommand(new Command("AB", this::getBalance));
        addCommand(new Command("BN", this::getBankNumberOfClients));
        addCommand(new Command("BC", this::getBankCode));
        addCommand(new Command("BA", this::getTotalBankAmount));
        addCommand(new Command("AL", this::listAllAccounts));
    }

    private void addCommand(Command command) {
        commands.add(command);
        commandMap.put(command.getCode(), command);
    }

    public void start() throws Exception {
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

    private void executeCommand(String[] args) throws Exception {
        if (args.length == 0) {
            writer.println("ER Neplatný příkaz.");
            return;
        }

        Command command = commandMap.get(args[0]);
        if (command != null) {
            command.execute(args);
        } else {
            writer.println("ER Neznámý příkaz.");
        }
    }

    private void getBankCode(String[] args) {
        writer.println("BC " + bankCode);
    }

    private void createAccount(String[] args) throws Exception {
        int accountNumber = generateAccountNumber();
        Account account = new Account(accountNumber, 0);
        accountService.createAccount(account);
        writer.println("AC " + accountNumber + "/" + bankCode);
    }

    private void getBalance(String[] args) throws Exception {
        if (args.length != 2) {
            writer.println("ER Formát čísla účtu není správný.");
            return;
        }

        Account account = findAccountByFormattedNumber(args[1]);
        if (account == null) {
            writer.println("ER Bankovní účet neexistuje.");
            return;
        }

        writer.println("AB " + account.getBalance());
    }

    private void deposit(String[] args) throws Exception {
        if (args.length != 3) {
            writer.println("ER číslo bankovního účtu a částka není ve správném formátu.");
            return;
        }

        Account account = findAccountByFormattedNumber(args[1]);
        if (account == null) {
            writer.println("ER Bankovní účet neexistuje.");
            return;
        }

        long amount = parseAmount(args[2]);
        account.deposit(amount);
        accountService.updateAccount(account);
        writer.println("AD");
    }

    private void withdraw(String[] args) throws Exception {
        if (args.length != 3) {
            writer.println("ER číslo bankovního účtu a částka není ve správném formátu.");
            return;
        }

        Account account = findAccountByFormattedNumber(args[1]);
        if (account == null) {
            writer.println("ER Bankovní účet neexistuje.");
            return;
        }

        long amount = parseAmount(args[2]);
        if (account.getBalance() < amount) {
            writer.println("ER Není dostatek finančních prostředků.");
            return;
        }

        account.withdraw(amount);
        accountService.updateAccount(account);
        writer.println("AW");
    }

    private void removeAccount(String[] args) throws Exception {
        if (args.length != 2) {
            writer.println("ER Formát čísla účtu není správný.");
            return;
        }

        Account account = findAccountByFormattedNumber(args[1]);
        if (account == null) {
            writer.println("ER Bankovní účet neexistuje.");
            return;
        }

        if (account.getBalance() != 0) {
            writer.println("ER Nelze smazat bankovní účet na kterém jsou finance.");
            return;
        }

        accountService.removeAccount(account.getAccountId());
        writer.println("AR");
    }

    private void getTotalBankAmount(String[] args) throws Exception {
        List<Account> accounts = accountService.getAllAccounts();
        long totalAmount = accounts.stream().mapToLong(Account::getBalance).sum();
        writer.println("BA " + totalAmount);
    }

    private void getBankNumberOfClients(String[] args) throws Exception {
        List<Account> accounts = accountService.getAllAccounts();
        writer.println("BN " + accounts.size());
    }

    private void listAllAccounts(String[] args) throws Exception {
        List<Account> accounts = accountService.getAllAccounts();
        writer.println("AL");
        for (Account account : accounts) {
            writer.println(account);
        }
    }

    private Account findAccountByFormattedNumber(String accountInfo) throws Exception {
        String[] parts = accountInfo.split("/");
        if (parts.length != 2 || !parts[1].equals(bankCode)) {
            return null;
        }

        int accountNumber = Integer.parseInt(parts[0]);
        return accountService.findAccountByNumber(accountNumber);
    }

    private int generateAccountNumber() {
        return (int) (Math.random() * 90000) + 10000;
    }

    private long parseAmount(String amountStr) {
        return Long.parseLong(amountStr);
    }
}
