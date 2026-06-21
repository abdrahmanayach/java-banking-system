package com.bankingcli.repository;

import com.bankingcli.model.Transaction;

import java.util.List;

public interface TransactionRepository {
    void save(Transaction transaction);
    List<Transaction> findAll();
    List<Transaction> findByAccountNumber(String accountNumber);
}
