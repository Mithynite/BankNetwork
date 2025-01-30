package bank.com.service;

import bank.com.core.EntityManager;
import bank.com.model.Transaction;

public class TransactionService {
    private final EntityManager entityManager;

    public TransactionService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void createTransaction(Transaction transaction) throws Exception {
        entityManager.persist(transaction);
    }
}
