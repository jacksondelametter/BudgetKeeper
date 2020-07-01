package service;

import model.Category;
import model.Transaction;

import java.sql.*;
import java.util.ArrayList;

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
                    "description STRING, amount DOUBLE, type STRING, id NOT NULL PRIMARY KEY);";
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

    public static ArrayList<Category> getCategories(String categoryType) {
        ArrayList<Category> categories = new ArrayList<>();
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            String query = String.format("SELECT * FROM categories WHERE type='%s';", categoryType);
            ResultSet results = stmt.executeQuery(query);
            while(results.next()) {
                String name = results.getString(1);
                String type  = results.getString(2);
                categories.add(new Category(name, type));
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return categories;
    }

    public static void deleteCategory(Category cat) {
        String deleteStatement = "DELETE FROM categories WHERE name=?";
        try (Connection conn = connect()){
            PreparedStatement stmt = conn.prepareStatement(deleteStatement);
            stmt.setString(1, cat.getName());
            stmt.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public static void addTransaction(Transaction transaction) {
        String transactionStmt = "INSERT INTO transactions (date, type, category, description, amount, id) " +
                "VALUES (?, ?, ?, ?, ?, ?);";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(transactionStmt)) {
            stmt.setDate(1, transaction.getDate());
            stmt.setString(2, transaction.getType());
            stmt.setString(3, transaction.getCategory());
            stmt.setString(4, transaction.getDescription());
            stmt.setDouble(5, transaction.getAmount());
            stmt.setString(6, transaction.getId());
            stmt.executeUpdate();

        } catch (Exception e) {
            System.out.println("Error adding transaction");
            System.out.println(e.getMessage());
        }
    }

    public static ArrayList<Transaction> getTransactions(String type) {
        String query = "SELECT * FROM transactions WHERE type=?;";
        ArrayList<Transaction> transactions = new ArrayList<>();
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, type);
            ResultSet results = stmt.executeQuery();
            while(results.next()) {
                Date transactionDate = results.getDate(1);
                String transactionType = results.getString(2);
                String category = results.getString(3);
                String description = results.getString(4);
                Double amount = results.getDouble(5);
                String id = results.getString(6);
                Transaction transaction = new Transaction(transactionDate, transactionType, category, description, amount, id);
                transactions.add(transaction);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return transactions;
    }

    public static void deleteTransaction(Transaction tran) {
        String deleteStatement = "DELETE FROM transactions WHERE id=?";
        try (Connection conn = connect()){
            PreparedStatement stmt = conn.prepareStatement(deleteStatement);
            stmt.setString(1,tran.getId());
            stmt.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

}
