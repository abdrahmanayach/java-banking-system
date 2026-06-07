package model;

import exception.InsufficientFundsException;
import model.enums.AccountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public abstract class Account {
    private final String id;
    private final String accountNumber;
    private final String ownerId;
    private final AccountType accountType;
    private final LocalDateTime createdAt;
    private BigDecimal balance;

    public Account(String ownerId, BigDecimal initialDeposit, AccountType accountType) {
        this.id = UUID.randomUUID().toString();
        this.accountNumber = "ACC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.ownerId = ownerId;
        this.balance = initialDeposit;
        this.accountType = accountType;
        this.createdAt = LocalDateTime.now();
    }

    protected Account(String id, String accountNumber, String ownerId,
                      BigDecimal balance, AccountType accountType, LocalDateTime createdAt) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.ownerId = ownerId;
        this.balance = balance;
        this.accountType = accountType;
        this.createdAt = createdAt;
    }

    public void deposit(BigDecimal amount) {
        balance = balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        if (amount.compareTo(balance) > 0) {
            throw new InsufficientFundsException("Insufficient funds");
        }
        balance = balance.subtract(amount);
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

    public BigDecimal getBalance() {
        return balance;
    }

    protected void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

}
