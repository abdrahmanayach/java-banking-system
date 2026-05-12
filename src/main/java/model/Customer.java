package model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Customer {
    private final String id;
    private final List<String> accountIds = new ArrayList<>();
    private String name;
    private String email;

    public Customer(String name, String email) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
    }

    public void addAccountId(String accountId) {
        accountIds.add(accountId);
    }

    public void removeAccountId(String accountId) {
        accountIds.remove(accountId);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getAccountIds() {
        return accountIds;
    }
}
