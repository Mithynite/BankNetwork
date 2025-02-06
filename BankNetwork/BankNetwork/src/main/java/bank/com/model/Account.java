package bank.com.model;

import bank.com.annotations.*;

/**
 * Class representing the account from the bank.
 * @author Jakub Hofman
*/
@Table(name = "accounts") // Table annotation for the "accounts" table
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id", canBeNull = false) // Primary key
    private int accountId;

    @Column(name = "account_number", canBeNull = false) // Column in table
    private int accountNumber;

    @Column(name = "balance", canBeNull = false) // Column in table
    private long balance;

    public Account(int accountNumber, long balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public Account() {
    }

    // Getters and Setters
    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }
    public void deposit(long amount) {
        this.balance += amount;
    }
    public void withdraw(long amount) {
        if (this.balance >= amount) {
            this.balance -= amount;
        } else {
            throw new IllegalArgumentException("Insufficient funds");
        }
    }

    @Override
    public String toString() {
        return "Account: number=" + accountNumber + ", balance=" + balance;
    }
}
