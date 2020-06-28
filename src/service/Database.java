package service;

import model.Category;
import model.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class Database {

    private static String dbUrl = "jdbc:sqlite:/home/jackson/Documents/BudgetKeeper/database/BudgetKeeper.db";

    private static Connection connect() throws Exception {
        Connection conn = DriverManager.getConnection(dbUrl);
        return conn;
    }

    static {
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            String transactionStatement = "CREATE TABLE IF NOT EXISTS transactions (date DATE, category STRING, " +
                    "description STRING, amount DOUBLE);";
            String categoryStatement = "CREATE TABLE IF NOT EXISTS categories (name STRING NOT NULL PRIMARY KEY, type STRING);";
            stmt.execute(transactionStatement);
            stmt.execute(categoryStatement);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public static void addCategory(Category category) {
        String categoryStmt = "INSERT INTO categories (name, type) VALUES (?, ?);";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(categoryStmt)) {
            stmt.setString(1, category.getName());
            stmt.setString(2, category.getType());
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static ArrayList<Category> getCategories() {
        ArrayList<Category> categories = new ArrayList<>();
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            String query = "SELECT * FROM categories;";
            ResultSet results = stmt.executeQuery(query);
            while(results.next()) {
                String name = results.getString(0);
                String type  = results.getString(1);
                categories.add(new Category(name, type));
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return categories;
    }

    public static void addTransaction(Transaction transaction) {
        String transactionStmt = "INSERT INTO transactions (date, category, description, amount, type) " +
                "VALUES (?, ?, ?, ?, ?);";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(transactionStmt)) {
            stmt.setDate(1, java.sql.Date.valueOf(transaction.getDate().toString()));
            stmt.setString(2, transaction.getCategory());
            stmt.setString(3, transaction.getDescription());
            stmt.setDouble(4, transaction.getAmount());
            stmt.setString(5, transaction.getType());
            stmt.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static ArrayList<Transaction> getTransactions() {
        ArrayList<Transaction> transactions = new ArrayList<>();
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            String query = "SELECT * FROM transactions;";
            ResultSet results = stmt.executeQuery(query);
            while(results.next()) {
                Date transactionDate = results.getDate(0);
                String transactionType = results.getString(1);
                String category = results.getString(2);
                String description = results.getString(3);
                Double amount = results.getDouble(4);
                Transaction transaction = new Transaction(transactionDate, transactionType, category, description, amount);
                transactions.add(transaction);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return transactions;
    }

}
