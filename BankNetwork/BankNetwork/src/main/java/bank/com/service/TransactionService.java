package bank.com.service;

import bank.com.core.EntityManager;
import bank.com.model.Transaction;

/**
 * This class provides services related to transactions.
 * @author Jakub Hofman
 */
public class TransactionService {
    private final EntityManager entityManager;

    /**
     * Constructs a new instance of TransactionService.
     *
     * @param entityManager The EntityManager used to interact with the database.
     */
    public TransactionService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Creates a new transaction in the database.
     *
     * @param transaction The transaction to be created.
     * @throws Exception If an error occurs while creating the transaction.
     */
    public void createTransaction(Transaction transaction) throws Exception {
        entityManager.persist(transaction);
    }
}
