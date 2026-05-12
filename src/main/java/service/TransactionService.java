package service;

import exception.AccountNotFoundException;
import model.Account;
import model.Transaction;
import model.enums.TransactionType;
import repository.AccountRepository;
import repository.TransactionRepository;

import java.util.List;

public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public Transaction deposit(String accountNumber, double amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountNumber));

        account.deposit(amount);
        accountRepository.save(account);

        Transaction transaction = new Transaction(
                TransactionType.DEPOSIT,
                amount,
                accountNumber,
                null,
                "Deposit of $" + String.format("%,.2f", amount)
        );
        transactionRepository.save(transaction);
        return transaction;
    }

    public Transaction withdraw(String accountNumber, double amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountNumber));

        account.withdraw(amount);
        accountRepository.save(account);

        Transaction transaction = new Transaction(
                TransactionType.WITHDRAWAL,
                amount,
                accountNumber,
                null,
                "Withdrawal of $" + String.format("%,.2f", amount)
        );
        transactionRepository.save(transaction);
        return transaction;
    }

    public Transaction transfer(String fromAccountNumber, String toAccountNumber, double amount) {
        Account fromAccount = accountRepository.findByAccountNumber(fromAccountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + fromAccountNumber));
        Account toAccount = accountRepository.findByAccountNumber(toAccountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + toAccountNumber));

        fromAccount.withdraw(amount);
        toAccount.deposit(amount);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        Transaction transaction = new Transaction(
                TransactionType.TRANSFER,
                amount,
                fromAccountNumber,
                toAccountNumber,
                "Transfer of $" + String.format("%,.2f", amount) + " from " + fromAccountNumber + " to " + toAccountNumber
        );
        transactionRepository.save(transaction);
        return transaction;
    }

    public List<Transaction> getHistory(String accountNumber) {
        return transactionRepository.findByAccountNumber(accountNumber);
    }
}
