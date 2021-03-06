package service;

import model.Category;
import model.Subscription;
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
            String transactionStatement = "CREATE TABLE IF NOT EXISTS transactions (date DATE, type STRING, " +
                    "category STRING, description STRING, amount DOUBLE, id NOT NULL PRIMARY KEY);";
            String categoryStatement = "CREATE TABLE IF NOT EXISTS categories (name STRING NOT NULL PRIMARY KEY, type STRING);";
            String subscriptionStatement = "CREATE TABLE IF NOT EXISTS subscriptions (id STRING NOT NULL PRIMARY KEY);";
            stmt.execute(transactionStatement);
            stmt.execute(categoryStatement);
            stmt.execute(subscriptionStatement);
        } catch (Exception e) {
            System.out.println("Error on database initialization");
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

    private static ArrayList<Category> getQueryCategories(ResultSet results) throws Exception {
        ArrayList<Category> categories = new ArrayList<>();
        while(results.next()) {
            String name = results.getString(1);
            String type  = results.getString(2);
            categories.add(new Category(name, type));
        }
        return categories;
    }

    public static ArrayList<Category> getCategories(String categoryType) {
        ArrayList<Category> categories = new ArrayList<>();
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            String query = String.format("SELECT * FROM categories WHERE type='%s' ORDER BY name;", categoryType);
            ResultSet results = stmt.executeQuery(query);
            categories = getQueryCategories(results);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return categories;
    }

    public static void deleteCategory(Category cat) {
        String catName = cat.getName();
        String deleteStatement = "DELETE FROM categories WHERE name=?";
        String otherCategoryStatement = "UPDATE transactions SET category=? WHERE category=?";
        try (Connection conn = connect()){
            PreparedStatement stmt = conn.prepareStatement(deleteStatement);
            PreparedStatement otherCatStmt = conn.prepareStatement(otherCategoryStatement);
            stmt.setString(1, catName);
            String otherCat = "";
            if(cat.getType().equals("Income")) {
                otherCat = "Other Income";
            }
            else {
                otherCat = "Other Receipt";
            }
            otherCatStmt.setString(1, otherCat);
            otherCatStmt.setString(2, catName);
            stmt.executeUpdate();
            otherCatStmt.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private static ArrayList<Transaction> getQueryTransactions(PreparedStatement stmt) throws Exception {
        ArrayList<Transaction> trans = new ArrayList<>();
        ResultSet results = stmt.executeQuery();
        while(results.next()) {
            Date transactionDate = results.getDate(1);
            String transactionType = results.getString(2);
            String category = results.getString(3);
            String description = results.getString(4);
            Double amount = results.getDouble(5);
            String id = results.getString(6);
            Transaction transaction = new Transaction(transactionDate, transactionType, category, description, amount, id);
            trans.add(transaction);
        }
        return trans;
    }

    public static ArrayList<Transaction> getTransactions(String type, Date startDate, Date endDate) {
        String query = "SELECT * FROM transactions WHERE type=? AND date>=? AND date<=? " +
                "ORDER BY category, amount;";
        ArrayList<Transaction> transactions = new ArrayList<>();
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, type);
            stmt.setDate(2, startDate);
            stmt.setDate(3, endDate);
            transactions = getQueryTransactions(stmt);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return transactions;
    }

    public static ArrayList<Transaction> getTransactionsByCategory(String type, String category) {
        ArrayList<Transaction> trans = new ArrayList<>();
        String query = String.format("SELECT * FROM transaction WHERE type=?, category=?;");
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, type);
            stmt.setString(2, category);
            trans = getQueryTransactions(stmt);
        } catch(Exception e) {
            System.out.println(e.toString());
        }
        return trans;
    }

    public static Transaction getTransactionById(String id) {
        String getTranStatement = "SELECT * FROM transactions WHERE id=?;";
        Transaction tran = null;
        try(Connection conn = connect();
        PreparedStatement stmt = conn.prepareStatement(getTranStatement)) {
            stmt.setString(1, id);
            ArrayList<Transaction> trans = getQueryTransactions(stmt);
            if(trans.size() > 0) {
                tran = trans.get(0);
            }
        } catch (Exception e) {
            System.out.println("Could not get transaction by id");
            System.out.println(e.toString());
        }
        return tran;
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

    public static void addSubscription(Subscription sub) {
        String addSubStatement = "INSERT INTO subscriptions (id) VALUES (?);";
        try(Connection conn = connect();
        PreparedStatement stmt = conn.prepareStatement(addSubStatement)) {
            stmt.setString(1, sub.getId());
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error adding subscription");
            System.out.println(e.toString());
        }
    }

    public static ArrayList<Subscription> getSubscriptions() {
        String getSubStatement = "SELECT * FROM subscriptions;";
        ArrayList<Subscription> subs = new ArrayList<>();
        try(Connection conn = connect();
        PreparedStatement stmt = conn.prepareStatement(getSubStatement)) {
            ResultSet results = stmt.executeQuery();
            while(results.next()) {
                String id = results.getString(1);
                subs.add(new Subscription(id));
            }
        } catch (Exception e) {
            System.out.println("Could not get subscriptions");
            System.out.println(e.toString());
        }
        return subs;
    }

    public static void deleteSubscriptionWithId(String id) {
        String getSubStatement = "DELETE FROM subscriptions WHERE id=?;";
        try(Connection conn = connect();
        PreparedStatement stmt = conn.prepareStatement(getSubStatement)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error deleting subscription by id");
            System.out.println(e.toString());
        }
    }

    public static void updateSubscriptionID(String oldId, String newId) {
        String updateStatement = "UPDATE subscriptions SET id=? WHERE id=?;";
        try(Connection conn = connect();
            PreparedStatement stmt = conn.prepareStatement(updateStatement)) {
            stmt.setString(1, newId);
            stmt.setString(2, oldId);
            stmt.executeUpdate();

        } catch (Exception e) {
            System.out.println("Error updating subscription with id");
            System.out.println(e.toString());
        }
    }

    public static void deleteTransaction(Transaction tran) {
        String deleteStatement = "DELETE FROM transactions WHERE id=?";
        String deleteCatStatement = "DELETE from categories WHERE name=? AND " +
                "NOT EXISTS (SELECT * FROM transactions WHERE category=?);";
        try (Connection conn = connect()){
            PreparedStatement stmt = conn.prepareStatement(deleteStatement);
            PreparedStatement catStmt = conn.prepareStatement(deleteCatStatement);
            stmt.setString(1,tran.getId());
            catStmt.setString(1, tran.getCategory());
            catStmt.setString(2, tran.getCategory());
            stmt.executeUpdate();
            catStmt.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

}
