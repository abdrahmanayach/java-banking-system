package com.bankingcli;

import com.bankingcli.repository.*;
import com.bankingcli.service.AccountService;
import com.bankingcli.service.CustomerService;
import com.bankingcli.service.TransactionService;
import com.bankingcli.util.MenuHandler;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        CustomerRepository customerRepository = new CustomerRepositoryImpl();
        AccountRepository accountRepository = new AccountRepositoryImpl();
        TransactionRepository transactionRepository = new TransactionRepositoryImpl();

        CustomerService customerService = new CustomerService(customerRepository);
        AccountService accountService = new AccountService(accountRepository, customerRepository);
        TransactionService transactionService = new TransactionService(accountRepository, transactionRepository);

        Scanner in = new Scanner(System.in);
        MenuHandler menuHandler = new MenuHandler(in, customerService, accountService, transactionService);
        menuHandler.start();
    }
}
