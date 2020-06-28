package service;

import model.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class Database {

    private static String dbUrl = "jdbc:sqlite:/home/jackson/Documents/BudgetKeeper/database/BudgetKeeper.db";

    static {
        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement()) {
            String statement = "CREATE TABLE IF NOT EXISTS transactions (date DATE, category STRING, " +
                    "description STRING, amount DOUBLE);";
            stmt.execute(statement);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public static void addTransaction(Transaction transaction) {
        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement()) {
            String values = transaction.getDate().toString() + "," +
                    transaction.getType() + "," + transaction.getCategory() + ","
                    + transaction.getDescription() + "," + transaction.getAmount();
            String command = "INSERT ONTO transactions (date, category, description, amount)" +
                    "VALUES (" + values + ");";
            stmt.execute(command);

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public static ArrayList<Transaction> getTransactions() {
        ArrayList<Transaction> transactions = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(dbUrl);
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
