package com.bankingcli.repository;

import com.bankingcli.model.Transaction;
import com.bankingcli.model.enums.TransactionType;
import com.bankingcli.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepositoryImpl implements TransactionRepository {

    @Override
    public void save(Transaction transaction) {
        String sql = """
                INSERT INTO transactions
                    (id, type, amount, source_account_number, target_account_number, created_at, description)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, transaction.getId());
            stmt.setString(2, transaction.getType().name());
            stmt.setBigDecimal(3, transaction.getAmount());
            stmt.setString(4, transaction.getSourceAccountNumber());
            stmt.setString(5, transaction.getTargetAccountNumber()); // null is fine here
            stmt.setTimestamp(6, Timestamp.valueOf(transaction.getCreatedAt()));
            stmt.setString(7, transaction.getDescription());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save transaction", e);
        }
    }

    @Override
    public List<Transaction> findAll() {
        String sql = "SELECT * FROM transactions ORDER BY created_at DESC";
        List<Transaction> transactions = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) transactions.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch transactions", e);
        }
        return transactions;
    }

    @Override
    public List<Transaction> findByAccountNumber(String accountNumber) {
        String sql = """
                SELECT * FROM transactions
                WHERE source_account_number = ? OR target_account_number = ?
                ORDER BY created_at DESC
                """;
        List<Transaction> transactions = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, accountNumber);
            stmt.setString(2, accountNumber);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) transactions.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find transactions by account", e);
        }
        return transactions;
    }

    private Transaction mapRow(ResultSet rs) throws SQLException {
        LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
        return new Transaction(
                rs.getString("id"),
                TransactionType.valueOf(rs.getString("type")),
                rs.getBigDecimal("amount"),
                rs.getString("source_account_number"),
                rs.getString("target_account_number"),
                createdAt,
                rs.getString("description")
        );
    }
}
