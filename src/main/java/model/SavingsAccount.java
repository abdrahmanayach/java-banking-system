package model;

import exception.DailyWithdrawalLimitException;
import exception.InsufficientFundsException;
import model.enums.AccountType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class SavingsAccount extends Account {
    private int dailyWithdrawalCount;
    private LocalDate lastWithdrawalDate;

    public SavingsAccount(String ownerId, BigDecimal initialDeposit) {
        super(ownerId, initialDeposit, AccountType.SAVINGS);
        this.dailyWithdrawalCount = 0;
        this.lastWithdrawalDate = null;
    }

    public SavingsAccount(String id, String accountNumber, String ownerId,
                          BigDecimal balance, LocalDateTime createdAt,
                          int dailyWithdrawalCount, LocalDate lastWithdrawalDate) {
        super(id, accountNumber, ownerId, balance, AccountType.SAVINGS, createdAt);
        this.dailyWithdrawalCount = dailyWithdrawalCount;
        this.lastWithdrawalDate = lastWithdrawalDate;
    }

    @Override
    public void withdraw(BigDecimal amount) {
        if (lastWithdrawalDate == null || !lastWithdrawalDate.equals(LocalDate.now())) {
            dailyWithdrawalCount = 0;
            lastWithdrawalDate = LocalDate.now();
        }

        if (dailyWithdrawalCount >= 3) {
            throw new DailyWithdrawalLimitException("Daily withdrawal limit of 3 reached for this savings account");
        }

        if (amount.compareTo(getBalance()) > 0) {
            throw new InsufficientFundsException("Insufficient funds");
        }

        setBalance(getBalance().subtract(amount));
        dailyWithdrawalCount++;
    }

    @Override
    public String getAccountInfo() {
        return String.format(
                "Account Number   : %s%n" +
                        "Type             : Savings%n" +
                        "Owner ID         : %s%n" +
                        "Balance          : $%,.2f%n" +
                        "Withdrawals Today: %d/3%n" +
                        "Created At       : %s",
                getAccountNumber(),
                getOwnerId(),
                getBalance(),
                dailyWithdrawalCount,
                getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        );
    }

    public int getDailyWithdrawalCount() {
        return dailyWithdrawalCount;
    }

    public LocalDate getLastWithdrawalDate() {
        return lastWithdrawalDate;
    }
}
