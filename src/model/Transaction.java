package model;

import java.util.Date;

public class Transaction {

    private Date date;
    private String category;
    private double amount;
    private String description;

    public Transaction(Date date, String category, String description, double amount) {
        this.date = date;
        this.category = category;
        this.amount = amount;
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
