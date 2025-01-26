package bank.com.model;

import bank.com.annotations.*;

import java.util.List;

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

    // One-to-Many relationship with Transaction
    @OneToMany(mappedBy = "accountNumber")
    private List<Transaction> transactions; // All transactions belonging to this account

    public Account(int accountId, int accountNumber, long balance) {
        this.accountId = accountId;
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
}
