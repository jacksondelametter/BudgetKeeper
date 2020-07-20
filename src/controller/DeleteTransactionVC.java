package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Transaction;
import service.Database;
import service.DateConverter;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

public class DeleteTransactionVC {

    @FXML
    public RadioButton incomeRadioButton;

    @FXML
    public RadioButton receiptRadioButton;

    @FXML
    public DatePicker incomeReceiptDatePicker;

    @FXML
    public TableView transactionTableView;

    private ToggleGroup incomeReceiptToggle;

    private void setupRadioButtons() {
        incomeReceiptToggle = new ToggleGroup();
        incomeRadioButton.setToggleGroup(incomeReceiptToggle);
        receiptRadioButton.setToggleGroup(incomeReceiptToggle);
        incomeRadioButton.fire();
    }

    private void setupDatePicker() {
        LocalDate currentDate = LocalDate.now();
        incomeReceiptDatePicker.setValue(currentDate);
    }

    @FXML
    public void initialize() {
        setupDatePicker();
        setupRadioButtons();
    }

    @FXML
    public void dateSelected(Event e) {
        RadioButton selectedCat = (RadioButton) incomeReceiptToggle.getSelectedToggle();
        selectedCat.fireEvent(new ActionEvent());
    }

    private void showMessage(String title, String message) throws Exception{
        Stage messageStage =  MessageVC.getMessageStage(title, message);
        messageStage.showAndWait();
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
                    // Sets listener for when a transaction gets deleted
                    Transaction tran = row.getItem();
                    Database.deleteTransaction(tran);
                    transactionTableView.getItems().remove(tran);
                    try {
                        showMessage("Deleted", "Deleted Transaction");
                    } catch (Exception e) {
                        System.out.println(e.toString());
                    }
                }
            });
            return row;
        });
    }

    @FXML
    public void incomeRadioButtonPressed(Event e) {
        Date rawDate = Date.valueOf(incomeReceiptDatePicker.getValue());
        Date startDate = DateConverter.getStartDate(rawDate);
        Date endDate = DateConverter.getEndDate(rawDate);
        System.out.println(startDate.toString());
        System.out.println(endDate.toString());
        ArrayList<Transaction> incomeTransaction = Database.getTransactions("Income", startDate, endDate);
        populateTransactionTable(incomeTransaction);
    }

    @FXML
    public void receiptRadioButtonPressed(Event e) {
        Date rawDate = Date.valueOf(incomeReceiptDatePicker.getValue());
        Date startDate = DateConverter.getStartDate(rawDate);
        Date endDate = DateConverter.getEndDate(rawDate);
        ArrayList<Transaction> receiptTransaction = Database.getTransactions("Receipt", startDate, endDate);
        populateTransactionTable(receiptTransaction);
    }
}
