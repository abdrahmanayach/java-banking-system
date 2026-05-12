package repository;

import model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {
    void save(Account account);
    Optional<Account> findById(String id);
    Optional<Account> findByAccountNumber(String accountNumber);
    List<Account> findAll();
    List<Account> findByOwnerId(String ownerId);
    void delete(String id);
}
