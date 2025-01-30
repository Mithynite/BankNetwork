package bank.com.model;

import bank.com.annotations.*;

@Table(name = "transactions") // Table annotation for the "transactions" table
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id", canBeNull = false) // Primary key
    private int transactionId;

    @Column(name = "account_number", canBeNull = false) // Foreign key reference to accounts
    private int accountNumber;

    @Column(name = "bank_code", canBeNull = false) // Bank code (LAN IP)
    private String bankCode;

    @Column(name = "transaction_type", canBeNull = false) // ENUM type ('create', 'remove', etc.)
    private String transactionType;

    @Column(name = "amount") // Transaction amount
    private long amount;

    @Column(name = "timestamp") // Timestamp of the transaction
    private String timestamp;

    @Transient // Not persisted, for runtime use only
    private Account account;

    public Transaction() {
    }

    public Transaction(int accountNumber, String bankCode, String transactionType, long amount, String timestamp) {
        this.accountNumber = accountNumber;
        this.bankCode = bankCode;
        this.transactionType = transactionType;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
