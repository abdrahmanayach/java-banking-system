package model;

import model.enums.AccountType;

import java.time.format.DateTimeFormatter;

public class CheckingAccount extends Account {

    public CheckingAccount(String ownerId, double initialDeposit) {
        super(ownerId, initialDeposit, AccountType.CHECKING);
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
