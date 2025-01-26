package bank.com.service;

import bank.com.core.EntityManager;

public class TransactionService {
    private final EntityManager entityManager;

    public TransactionService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
