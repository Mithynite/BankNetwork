package bank.com.service;

import bank.com.core.EntityManager;
import bank.com.model.Account;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides services related to bank accounts.
 * @author Jakub Hofman
 */
public class AccountService {
    private final EntityManager entityManager;

    /**
     * Constructs an AccountService instance.
     *
     * @param entityManager The EntityManager used to interact with the database.
     */
    public AccountService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Creates a new account in the database.
     *
     * @param account The account to be created.
     * @throws Exception If an error occurs during the database operation.
     */
    public void createAccount(Account account) throws Exception {
        entityManager.persist(account);
    }

    /**
     * Removes an account from the database.
     *
     * @param accountId The ID of the account to be removed.
     * @throws Exception If an error occurs during the database operation.
     */
    public void removeAccount(int accountId) throws Exception {
        entityManager.delete(Account.class, accountId);
    }

    /**
     * Updates an existing account in the database.
     *
     * @param account The updated account.
     * @throws Exception If an error occurs during the database operation.
     */
    public void updateAccount(Account account) throws Exception {
        entityManager.update(account);
    }

    /**
     * Finds an account by its account number.
     *
     * @param accountNumber The account number to search for.
     * @return The found account, or null if no account is found.
     * @throws Exception If an error occurs during the database operation.
     */
    public Account findAccountByNumber(int accountNumber) throws Exception {
        String sql = "SELECT * FROM accounts WHERE account_number = ?";
        List<Account> accounts = entityManager.query(Account.class, sql, List.of(accountNumber));

        return accounts.isEmpty() ? null : accounts.get(0);
    }

    /**
     * Retrieves all accounts from the database.
     *
     * @return A list of all accounts.
     * @throws Exception If an error occurs during the database operation.
     */
    public List<Account> getAllAccounts() throws Exception {
        String query = "SELECT * FROM accounts";
        return entityManager.query(Account.class, query, List.of());
    }
}
