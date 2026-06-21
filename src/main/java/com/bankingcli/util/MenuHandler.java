package com.bankingcli.util;

import com.bankingcli.model.Account;
import com.bankingcli.model.Customer;
import com.bankingcli.model.Transaction;
import com.bankingcli.model.enums.AccountType;
import com.bankingcli.service.AccountService;
import com.bankingcli.service.CustomerService;
import com.bankingcli.service.TransactionService;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class MenuHandler {

    private final Scanner scanner;
    private final CustomerService customerService;
    private final AccountService accountService;
    private final TransactionService transactionService;

    public MenuHandler(Scanner scanner, CustomerService customerService,
                       AccountService accountService, TransactionService transactionService) {
        this.scanner = scanner;
        this.customerService = customerService;
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    public void start() {
        System.out.println("Welcome to Banking CLI");
        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> handleRegisterCustomer();
                case "2" -> handleListCustomers();
                case "3" -> handleOpenAccount();
                case "4" -> handleViewAccounts();
                case "5" -> handleDeposit();
                case "6" -> handleWithdraw();
                case "7" -> handleTransfer();
                case "8" -> handleTransactionHistory();
                case "9" -> handleCheckBalance();
                case "10" -> handleCloseAccount();
                case "0" -> running = false;
                default -> System.out.println("Invalid option, please try again.");
            }
        }
        System.out.println("Goodbye!");
        scanner.close();
    }

    private void printMenu() {
        System.out.println("\n===== BANKING CLI =====");
        System.out.println("1.  Register Customer");
        System.out.println("2.  List Customers");
        System.out.println("3.  Open Account");
        System.out.println("4.  View Accounts");
        System.out.println("5.  Deposit");
        System.out.println("6.  Withdraw");
        System.out.println("7.  Transfer");
        System.out.println("8.  Transaction History");
        System.out.println("9.  Check Balance");
        System.out.println("10. Close Account");
        System.out.println("0.  Exit");
        System.out.print("Choose an option: ");
    }

    private void handleRegisterCustomer() {
        try {
            System.out.print("Enter name: ");
            String name = scanner.nextLine().trim();
            System.out.print("Enter email: ");
            String email = scanner.nextLine().trim();
            Customer customer = customerService.registerCustomer(name, email);
            System.out.println("Customer registered successfully.");
            System.out.println("Customer ID: " + customer.getId());
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleListCustomers() {
        try {
            List<Customer> customers = customerService.listAllCustomers();
            if (customers.isEmpty()) {
                System.out.println("No customers found.");
                return;
            }
            customers.forEach(c -> System.out.println(
                    "ID: " + c.getId() + " | Name: " + c.getName() + " | Email: " + c.getEmail()
            ));
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleOpenAccount() {
        try {
            System.out.print("Enter customer ID: ");
            String customerId = scanner.nextLine().trim();
            System.out.println("Account type: 1. Savings  2. Checking");
            System.out.print("Choose type: ");
            String typeChoice = scanner.nextLine().trim();
            AccountType type = typeChoice.equals("1") ? AccountType.SAVINGS : AccountType.CHECKING;
            System.out.print("Enter initial deposit: ");
            BigDecimal initialDeposit = new BigDecimal(scanner.nextLine().trim());
            Account account = accountService.openAccount(customerId, type, initialDeposit);
            System.out.println("Account opened successfully.");
            System.out.println(account.getAccountInfo());
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount entered.");
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleViewAccounts() {
        try {
            System.out.print("Enter customer ID: ");
            String customerId = scanner.nextLine().trim();
            List<Account> accounts = accountService.findAccountsByCustomer(customerId);
            if (accounts.isEmpty()) {
                System.out.println("No accounts found for this customer.");
                return;
            }
            accounts.forEach(a -> System.out.println(a.getAccountInfo() + "\n"));
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleDeposit() {
        try {
            System.out.print("Enter account number: ");
            String accountNumber = scanner.nextLine().trim();
            System.out.print("Enter amount: ");
            BigDecimal amount = new BigDecimal(scanner.nextLine().trim());
            transactionService.deposit(accountNumber, amount);
            System.out.println("Deposit successful.");
            System.out.println("New balance: $" + String.format("%,.2f", accountService.getBalance(accountNumber)));
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount entered.");
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleWithdraw() {
        try {
            System.out.print("Enter account number: ");
            String accountNumber = scanner.nextLine().trim();
            System.out.print("Enter amount: ");
            BigDecimal amount = new BigDecimal(scanner.nextLine().trim());
            transactionService.withdraw(accountNumber, amount);
            System.out.println("Withdrawal successful.");
            System.out.println("New balance: $" + String.format("%,.2f", accountService.getBalance(accountNumber)));
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount entered.");
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleTransfer() {
        try {
            System.out.print("Enter source account number: ");
            String fromAccount = scanner.nextLine().trim();
            System.out.print("Enter target account number: ");
            String toAccount = scanner.nextLine().trim();
            System.out.print("Enter amount: ");
            BigDecimal amount = new BigDecimal(scanner.nextLine().trim());
            transactionService.transfer(fromAccount, toAccount, amount);
            System.out.println("Transfer successful.");
            System.out.println("New balance: $" + String.format("%,.2f", accountService.getBalance(fromAccount)));
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount entered.");
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleTransactionHistory() {
        try {
            System.out.print("Enter account number: ");
            String accountNumber = scanner.nextLine().trim();
            List<Transaction> transactions = transactionService.getHistory(accountNumber);
            if (transactions.isEmpty()) {
                System.out.println("No transactions found.");
                return;
            }
            transactions.forEach(t -> System.out.println(
                    t.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) +
                            " | " + t.getType() +
                            " | $" + String.format("%,.2f", t.getAmount()) +
                            " | " + t.getDescription()
            ));
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleCheckBalance() {
        try {
            System.out.print("Enter account number: ");
            String accountNumber = scanner.nextLine().trim();
            BigDecimal balance = accountService.getBalance(accountNumber);
            System.out.println("Current balance: $" + String.format("%,.2f", balance));
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleCloseAccount() {
        try {
            System.out.print("Enter account number: ");
            String accountNumber = scanner.nextLine().trim();
            accountService.closeAccount(accountNumber);
            System.out.println("Account closed successfully.");
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
