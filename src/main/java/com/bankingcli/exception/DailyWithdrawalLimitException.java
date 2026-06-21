package com.bankingcli.exception;

public class DailyWithdrawalLimitException extends RuntimeException {
    public DailyWithdrawalLimitException(String message) {
        super(message);
    }
}
