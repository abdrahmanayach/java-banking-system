package com.bankingcli.service;

import com.bankingcli.exception.AccountNotFoundException;
import com.bankingcli.exception.CustomerNotFoundException;
import com.bankingcli.model.Account;
import com.bankingcli.model.CheckingAccount;
import com.bankingcli.model.SavingsAccount;
import com.bankingcli.model.enums.AccountType;
import com.bankingcli.repository.AccountRepository;
import com.bankingcli.repository.CustomerRepository;

import java.math.BigDecimal;
import java.util.List;

public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    public AccountService(AccountRepository accountRepository, CustomerRepository customerRepository) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
    }

    public Account openAccount(String customerId, AccountType type, BigDecimal initialDeposit) {
        customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + customerId));

        Account account;
        if (type == AccountType.SAVINGS) {
            account = new SavingsAccount(customerId, initialDeposit);
        } else {
            account = new CheckingAccount(customerId, initialDeposit);
        }

        accountRepository.save(account);
        return account;
    }

    public void closeAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountNumber));

        if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new IllegalStateException("Account balance must be zero before closing");
        }

        accountRepository.delete(accountNumber);
    }

    public BigDecimal getBalance(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountNumber))
                .getBalance();
    }

    public List<Account> findAccountsByCustomer(String customerId) {
        return accountRepository.findByOwnerId(customerId);
    }

    public Account findByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountNumber));
    }
}
