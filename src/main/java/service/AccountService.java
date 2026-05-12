package service;

import exception.AccountNotFoundException;
import exception.CustomerNotFoundException;
import model.Account;
import model.CheckingAccount;
import model.Customer;
import model.SavingsAccount;
import model.enums.AccountType;
import repository.AccountRepository;
import repository.CustomerRepository;

import java.util.List;

public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    public AccountService(AccountRepository accountRepository, CustomerRepository customerRepository) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
    }

    public Account openAccount(String customerId, AccountType type, double initialDeposit) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + customerId));

        Account account;
        if (type == AccountType.SAVINGS) {
            account = new SavingsAccount(customerId, initialDeposit);
        } else {
            account = new CheckingAccount(customerId, initialDeposit);
        }

        customer.addAccountId(account.getId());
        accountRepository.save(account);
        customerRepository.save(customer);
        return account;
    }

    public void closeAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountNumber));

        if (account.getBalance() != 0) {
            throw new IllegalStateException("Account balance must be zero before closing");
        }

        Customer customer = customerRepository.findById(account.getOwnerId())
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        customer.removeAccountId(account.getId());
        customerRepository.save(customer);
        accountRepository.delete(accountNumber);
    }

    public double getBalance(String accountNumber) {
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
