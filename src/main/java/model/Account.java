package model;

import exception.InsufficientFundsException;
import model.enums.AccountType;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class Account {
    private final String id;
    private final String accountNumber;
    private final String ownerId;
    private final AccountType accountType;
    private final LocalDateTime createdAt;
    private double balance;

    public Account(String ownerId, double initialDeposit, AccountType accountType) {
        this.id = UUID.randomUUID().toString();
        this.accountNumber = "ACC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.ownerId = ownerId;
        this.balance = initialDeposit;
        this.accountType = accountType;
        this.createdAt = LocalDateTime.now();
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public void withdraw(double amount) {
        if (amount > balance) {
            throw new InsufficientFundsException("Insufficient funds");
        }
        balance -= amount;
    }

    public abstract String getAccountInfo();

    public String getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public double getBalance() {
        return balance;
    }

    protected void setBalance(double balance) {
        this.balance = balance;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

}
