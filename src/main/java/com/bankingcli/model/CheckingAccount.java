package com.bankingcli.model;

import com.bankingcli.model.enums.AccountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CheckingAccount extends Account {

    public CheckingAccount(String ownerId, BigDecimal initialDeposit) {
        super(ownerId, initialDeposit, AccountType.CHECKING);
    }

    public CheckingAccount(String id, String accountNumber, String ownerId,
                           BigDecimal balance, LocalDateTime createdAt) {
        super(id, accountNumber, ownerId, balance, AccountType.CHECKING, createdAt);
    }

    @Override
    public String getAccountInfo() {
        return String.format(
                "Account Number  : %s%n" +
                        "Type            : Checking%n" +
                        "Owner ID        : %s%n" +
                        "Balance         : $%,.2f%n" +
                        "Created At      : %s",
                getAccountNumber(),
                getOwnerId(),
                getBalance(),
                getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        );
    }
}
