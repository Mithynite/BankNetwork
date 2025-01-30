package bank.com.service;

import bank.com.core.EntityManager;
import bank.com.model.Account;
import java.util.ArrayList;
import java.util.List;

public class AccountService {
    private final EntityManager entityManager;

    public AccountService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void createAccount(Account account) throws Exception {
        entityManager.persist(account);
    }

    public void removeAccount(int accountId) throws Exception {
        entityManager.delete(Account.class, accountId);
    }

    public void updateAccount(Account account) throws Exception {
        entityManager.update(account);
    }

    public Account findAccountByNumber(int accountNumber) throws Exception {
        String sql = "SELECT * FROM accounts WHERE account_number = ?";
        List<Account> accounts = entityManager.query(Account.class, sql, List.of(accountNumber));

        return accounts.isEmpty() ? null : accounts.get(0);
    }

    public List<Account> getAllAccounts() throws Exception {
        String query = "SELECT * FROM accounts";
        return entityManager.query(Account.class, query, List.of());
    }

}
