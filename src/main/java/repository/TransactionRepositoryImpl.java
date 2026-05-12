package repository;

import com.google.gson.reflect.TypeToken;
import model.Transaction;
import util.JsonFileManager;

import java.util.Comparator;
import java.util.List;

public class TransactionRepositoryImpl extends BaseRepository<Transaction> implements TransactionRepository {

    public TransactionRepositoryImpl(JsonFileManager fileManager) {
        super("data/transactions.json", fileManager, new TypeToken<List<Transaction>>() {
        }.getType());
    }

    @Override
    public void save(Transaction transaction) {
        List<Transaction> transactions = loadAll();
        transactions.add(transaction);
        saveAll(transactions);
    }

    @Override
    public List<Transaction> findAll() {
        return loadAll();
    }

    @Override
    public List<Transaction> findByAccountNumber(String accountNumber) {
        List<Transaction> transactions = loadAll();
        return transactions.stream().filter(t -> accountNumber.equals(t.getSourceAccountNumber()) ||
                        accountNumber.equals(t.getTargetAccountNumber()))
                .sorted(Comparator.comparing(Transaction::getCreatedAt).reversed())
                .toList();
    }
}
