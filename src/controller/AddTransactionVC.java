package controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Category;
import model.Transaction;
import service.Database;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

public class AddTransactionVC {

    @FXML
    public RadioButton incomeRadioButton;

    @FXML
    public RadioButton receiptRadioButton;

    @FXML
    public DatePicker datePicker;

    @FXML
    public ChoiceBox<String> categoryChoiceBox;

    @FXML
    public TextField enterDescription;

    @FXML
    public TextField enterAmount;

    @FXML
    public Button addButton;

    private ToggleGroup incomeReceiptToggle;

    private void setupRadioButtons() {
        incomeReceiptToggle = new ToggleGroup();
        incomeRadioButton.setToggleGroup(incomeReceiptToggle);
        receiptRadioButton.setToggleGroup(incomeReceiptToggle);
        incomeRadioButton.fire();
    }

    private void setupDatePicker() {
        LocalDate currentDate = LocalDate.now();
        datePicker.setValue(currentDate);
    }

    @FXML
    public void initialize() {
        setupRadioButtons();
        setupDatePicker();
    }

    private void addCategoriesToChoiceBox(ArrayList<Category> categories) {
        categoryChoiceBox.getItems().clear();
        for(Category cat : categories) {
            categoryChoiceBox.getItems().add(cat.getName());
        }
    }

    @FXML
    public void incomeRadioButtonPressed(Event e) {
        ArrayList<Category> incomeCat = Database.getCategories("Income");
        addCategoriesToChoiceBox(incomeCat);
    }

    @FXML
    public void receiptRadioButtonPressed(Event e) {
        ArrayList<Category> receiptCat = Database.getCategories("Receipt");
        addCategoriesToChoiceBox(receiptCat);
    }

    @FXML
    public void addButtonPressed(Event e) {
        String description = enterDescription.getText();
        String amount = enterAmount.getText();
        if (description.equals("") && amount.equals("")) {
            return;
        }
        double amountNum = 0.0;
        try {
            amountNum = Double.parseDouble(amount);
        } catch (Exception error) {
            System.out.println(error.toString());
            return;
        }
        Date date = Date.valueOf(datePicker.getValue().toString());
        String category = categoryChoiceBox.getSelectionModel().getSelectedItem();
        RadioButton transactionTypeRadio = ((RadioButton) incomeReceiptToggle.getSelectedToggle());
        String type = transactionTypeRadio.getText();
        String id = UUID.randomUUID().toString();
        Transaction t = new Transaction(date, type, category, description, amountNum, id);
        Database.addTransaction(t);
        Stage stage = (Stage) addButton.getScene().getWindow();
        stage.close();
    }
}
