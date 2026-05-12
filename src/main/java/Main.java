import repository.*;
import service.AccountService;
import service.CustomerService;
import service.TransactionService;
import util.JsonFileManager;
import util.MenuHandler;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        JsonFileManager fileManager = new JsonFileManager();

        CustomerRepository customerRepository = new CustomerRepositoryImpl(fileManager);
        AccountRepository accountRepository = new AccountRepositoryImpl(fileManager);
        TransactionRepository transactionRepository = new TransactionRepositoryImpl(fileManager);

        CustomerService customerService = new CustomerService(customerRepository);
        AccountService accountService = new AccountService(accountRepository, customerRepository);
        TransactionService transactionService = new TransactionService(accountRepository, transactionRepository);

        Scanner in = new Scanner(System.in);
        MenuHandler menuHandler = new MenuHandler(in, customerService, accountService, transactionService);
        menuHandler.start();
    }
}
