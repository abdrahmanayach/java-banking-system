package repository;

import com.google.gson.reflect.TypeToken;
import model.Account;
import util.JsonFileManager;

import java.util.List;
import java.util.Optional;

public class AccountRepositoryImpl extends BaseRepository<Account> implements AccountRepository {

    public AccountRepositoryImpl(JsonFileManager fileManager) {
        super("data/accounts.json", fileManager, new TypeToken<List<Account>>() {
        }.getType());
    }

    @Override
    public void save(Account account) {
        List<Account> accounts = loadAll();
        accounts.removeIf(a -> a.getId().equals(account.getId()));
        accounts.add(account);
        saveAll(accounts);
    }

    @Override
    public Optional<Account> findById(String id) {
        List<Account> accounts = loadAll();
        return accounts.stream().filter(a -> a.getId().equals(id)).findFirst();
    }

    @Override
    public Optional<Account> findByAccountNumber(String accountNumber) {
        List<Account> accounts = loadAll();
        return accounts.stream()
                .filter(a -> a.getAccountNumber().equals(accountNumber))
                .findFirst();
    }

    @Override
    public List<Account> findAll() {
        return loadAll();
    }

    @Override
    public List<Account> findByOwnerId(String ownerId) {
        List<Account> accounts = loadAll();
        return accounts.stream()
                .filter(a -> a.getOwnerId().equals(ownerId))
                .findFirst()
                .stream()
                .toList();
    }

    @Override
    public void delete(String accountNumber) {
        List<Account> accounts = loadAll();
        accounts.removeIf(a -> a.getAccountNumber().equals(accountNumber));
        saveAll(accounts);
    }
}
