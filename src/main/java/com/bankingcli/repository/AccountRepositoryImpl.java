package com.bankingcli.repository;

import com.bankingcli.model.Account;
import com.bankingcli.model.CheckingAccount;
import com.bankingcli.model.SavingsAccount;
import com.bankingcli.model.enums.AccountType;
import com.bankingcli.util.DatabaseConnection;

import java.math.BigDecimal;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountRepositoryImpl implements AccountRepository {

    @Override
    public void save(Account account) {
        String sql = """
                INSERT INTO accounts
                    (id, account_number, owner_id, account_type, balance, created_at,
                     daily_withdrawal_count, last_withdrawal_date)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                ON CONFLICT (id) DO UPDATE SET
                    balance                = EXCLUDED.balance,
                    daily_withdrawal_count = EXCLUDED.daily_withdrawal_count,
                    last_withdrawal_date   = EXCLUDED.last_withdrawal_date
                """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, account.getId());
            stmt.setString(2, account.getAccountNumber());
            stmt.setString(3, account.getOwnerId());
            stmt.setString(4, account.getAccountType().name());
            stmt.setBigDecimal(5, account.getBalance());
            stmt.setTimestamp(6, Timestamp.valueOf(account.getCreatedAt()));

            if (account instanceof SavingsAccount sa) {
                stmt.setInt(7, sa.getDailyWithdrawalCount());
                LocalDate lastDate = sa.getLastWithdrawalDate();
                stmt.setObject(8, lastDate != null ? Date.valueOf(lastDate) : null);
            } else {
                stmt.setNull(7, Types.INTEGER);
                stmt.setNull(8, Types.DATE);
            }

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save account", e);
        }
    }

    @Override
    public Optional<Account> findById(String id) {
        String sql = "SELECT * FROM accounts WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find account by id", e);
        }
    }

    @Override
    public Optional<Account> findByAccountNumber(String accountNumber) {
        String sql = "SELECT * FROM accounts WHERE account_number = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find account by number", e);
        }
    }

    @Override
    public List<Account> findAll() {
        String sql = "SELECT * FROM accounts";
        List<Account> accounts = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) accounts.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch accounts", e);
        }
        return accounts;
    }

    @Override
    public List<Account> findByOwnerId(String ownerId) {
        String sql = "SELECT * FROM accounts WHERE owner_id = ?";
        List<Account> accounts = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ownerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) accounts.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find accounts by owner", e);
        }
        return accounts;
    }

    @Override
    public void delete(String accountNumber) {
        String sql = "DELETE FROM accounts WHERE account_number = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, accountNumber);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete account", e);
        }
    }

    private Account mapRow(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        String accountNumber = rs.getString("account_number");
        String ownerId = rs.getString("owner_id");
        BigDecimal balance = rs.getBigDecimal("balance");
        LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
        AccountType type = AccountType.valueOf(rs.getString("account_type"));

        if (type == AccountType.SAVINGS) {
            int dailyCount = rs.getInt("daily_withdrawal_count");
            Date sqlDate = rs.getDate("last_withdrawal_date");
            LocalDate lastDate = sqlDate != null ? sqlDate.toLocalDate() : null;
            return new SavingsAccount(id, accountNumber, ownerId, balance, createdAt, dailyCount, lastDate);
        } else {
            return new CheckingAccount(id, accountNumber, ownerId, balance, createdAt);
        }
    }
}
