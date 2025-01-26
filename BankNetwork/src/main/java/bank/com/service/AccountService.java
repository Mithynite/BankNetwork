package bank.com.service;

import bank.com.core.EntityManager;

public class AccountService {
    private final EntityManager entityManager;

    public AccountService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
