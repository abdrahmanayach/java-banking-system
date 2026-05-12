package model;

import model.enums.TransactionType;

import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {
    private final String id;
    private final TransactionType type;
    private final double amount;
    private final String sourceAccountNumber;
    private final String targetAccountNumber;
    private final LocalDateTime createdAt;
    private final String description;

    public Transaction(TransactionType type, double amount, String sourceAccountNumber, String targetAccountNumber, String description) {
        this.id = UUID.randomUUID().toString();
        this.type = type;
        this.amount = amount;
        this.sourceAccountNumber = sourceAccountNumber;
        this.targetAccountNumber = targetAccountNumber;
        this.createdAt = LocalDateTime.now();
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getSourceAccountNumber() {
        return sourceAccountNumber;
    }

    public TransactionType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getTargetAccountNumber() {
        return targetAccountNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
