package repository;

import com.google.gson.reflect.TypeToken;
import model.Customer;
import util.JsonFileManager;

import java.util.List;
import java.util.Optional;

public class CustomerRepositoryImpl extends BaseRepository<Customer> implements CustomerRepository {

    public CustomerRepositoryImpl(JsonFileManager fileManager) {
        super("data/customers.json", fileManager, new TypeToken<List<Customer>>() {
        }.getType());
    }

    @Override
    public void save(Customer customer) {
        List<Customer> customers = loadAll();
        customers.removeIf(c -> c.getId().equals(customer.getId()));
        customers.add(customer);
        saveAll(customers);
    }

    @Override
    public Optional<Customer> findById(String id) {
        List<Customer> customers = loadAll();
        return customers.stream().filter(c -> c.getId().equals(id)).findFirst();
    }

    @Override
    public List<Customer> findAll() {
        return loadAll();
    }

    @Override
    public void delete(String id) {
        List<Customer> customers = loadAll();
        customers.removeIf(c -> c.getId().equals(id));
        saveAll(customers);
    }
}
