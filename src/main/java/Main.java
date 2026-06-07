import repository.*;
import service.AccountService;
import service.CustomerService;
import service.TransactionService;
import util.MenuHandler;

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
