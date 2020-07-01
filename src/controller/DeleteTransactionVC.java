package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Transaction;
import service.Database;

import java.sql.Date;
import java.util.ArrayList;

public class DeleteTransactionVC {

    @FXML
    public RadioButton incomeRadioButton;

    @FXML
    public RadioButton receiptRadioButton;

    @FXML
    public TableView transactionTableView;

    private ToggleGroup incomeReceiptToggle;

    private void setupRadioButtons() {
        incomeReceiptToggle = new ToggleGroup();
        incomeRadioButton.setToggleGroup(incomeReceiptToggle);
        receiptRadioButton.setToggleGroup(incomeReceiptToggle);
        incomeRadioButton.fire();
    }

    @FXML
    public void initialize() {
        setupRadioButtons();
    }

    private void populateTransactionTable(ArrayList<Transaction> trans) {
        transactionTableView.getColumns().clear();
        transactionTableView.getItems().clear();
        TableColumn<Date, Transaction> dateCol = new TableColumn<>("Date");
        TableColumn<String, Transaction> catCol = new TableColumn<>("Category");
        TableColumn<String, Transaction> descCol = new TableColumn<>("Desc");
        TableColumn<Double, Transaction> amountCol = new TableColumn<>("Amount");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        catCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        ObservableList colList = FXCollections.observableArrayList(dateCol, catCol, descCol, amountCol);
        transactionTableView.getColumns().addAll(colList);
        for (Transaction tran : trans) {
            transactionTableView.getItems().add(tran);
        }
        transactionTableView.setRowFactory(tableView -> {
            TableRow<Transaction> row = new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {
                if(mouseEvent.getClickCount() == 2 && !row.isEmpty()) {
                    Transaction tran = row.getItem();
                    Database.deleteTransaction(tran);
                    transactionTableView.getItems().remove(tran);
                }
            });
            return row;
        });
    }

    @FXML
    public void incomeRadioButtonPressed(Event e) {
        ArrayList<Transaction> incomeTransaction = Database.getTransactions("Income");
        populateTransactionTable(incomeTransaction);
    }

    @FXML
    public void receiptRadioButtonPressed(Event e) {
        ArrayList<Transaction> receiptTransaction = Database.getTransactions("Receipt");
        populateTransactionTable(receiptTransaction);
    }
}
