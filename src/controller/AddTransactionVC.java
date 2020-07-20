package controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Category;
import model.Transaction;
import service.Database;

import java.io.File;
import java.net.URL;
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
    public RadioButton chooseCatRadioButton;

    @FXML
    public RadioButton addCatRadioButton;

    @FXML
    public HBox categoryGroup;

    private ChoiceBox<String> chooseCatChoiceBox;

    private TextField addCatField;

    @FXML
    public TextField enterDescription;

    @FXML
    public TextField enterAmount;

    @FXML
    public Button addButton;

    private ToggleGroup incomeReceiptToggle;

    private ToggleGroup categoryToggle;

    private void setupRadioButtons() {
        incomeReceiptToggle = new ToggleGroup();
        incomeRadioButton.setToggleGroup(incomeReceiptToggle);
        receiptRadioButton.setToggleGroup(incomeReceiptToggle);
        incomeRadioButton.fire();

        categoryToggle = new ToggleGroup();
        chooseCatRadioButton.setToggleGroup(categoryToggle);
        addCatRadioButton.setToggleGroup(categoryToggle);
        chooseCatRadioButton.fire();
    }

    private void setupDatePicker() {
        LocalDate currentDate = LocalDate.now();
        datePicker.setValue(currentDate);
    }

    private void setupCategoryGroup() {
        chooseCatChoiceBox = new ChoiceBox<>();
        addCatField = new TextField();
        addCatField.setPromptText("Add Category");
    }

    @FXML
    public void initialize() {
        setupCategoryGroup();
        setupRadioButtons();
        setupDatePicker();
    }

    private String getTransactionType() {
        RadioButton transactionTypeRadio = (RadioButton) incomeReceiptToggle.getSelectedToggle();
        return transactionTypeRadio.getText();
    }

    private boolean addCategory() {
        String name = addCatField.getText();
        if (!name.equals("")) {
            String type = getTransactionType();
            Category c = new Category(name, type);
            Database.addCategory(c);
            return true;
        }
        else {
            return false;
        }
    }

    private String getCategory() {
        RadioButton categoryButton = (RadioButton) categoryToggle.getSelectedToggle();
        if(categoryButton == chooseCatRadioButton) {
            return chooseCatChoiceBox.getSelectionModel().getSelectedItem();
        }
        else if(addCategory()) {
            return addCatField.getText();
        }
        else {
            return null;
        }
    }

    private void updateChooseCatChoiceBox() {
        chooseCatChoiceBox.getItems().clear();
        String type = getTransactionType();
        ArrayList<Category> cats = Database.getCategories(type);
        for(Category cat : cats) {
            chooseCatChoiceBox.getItems().add(cat.getName());
        }
    }

    @FXML
    public void incomeRadioButtonPressed() {
        updateChooseCatChoiceBox();
    }

    @FXML
    public void receiptRadioButtonPressed() {
        updateChooseCatChoiceBox();
    }

    private void showMessage(String title, String message) throws Exception{
        Stage messageStage =  MessageVC.getMessageStage(title, message);
        messageStage.showAndWait();
    }


    @FXML
    public void addButtonPressed(Event e) throws Exception{
        String description = enterDescription.getText();
        String amount = enterAmount.getText();
        if (description.equals("") && amount.equals("")) {
            showMessage("Error", "Could not add transaction: description is empty");
            return;
        }
        double amountNum = 0.0;
        try {
            amountNum = Double.parseDouble(amount);
        } catch (Exception error) {
            showMessage("Error", "Could not add transaction: amount is not a number");
            return;
        }
        Date date = Date.valueOf(datePicker.getValue().toString());
        String category = getCategory();
        if(category != null) {
            String type = getTransactionType();
            String id = UUID.randomUUID().toString();
            Transaction t = new Transaction(date, type, category, description, amountNum, id);
            Database.addTransaction(t);
            showMessage("Added", "Added Transaction");
            Stage stage = (Stage) addButton.getScene().getWindow();
            stage.close();
        }
        else {
            showMessage("Error", "Could not add transaction: Error with category name");
        }
    }

    private void addToCatGroup(Node node) {
        categoryGroup.getChildren().clear();
        categoryGroup.getChildren().add(node);
    }

    @FXML
    public void chooseCatPressed(Event e) {
        updateChooseCatChoiceBox();
        addToCatGroup(chooseCatChoiceBox);
    }

    @FXML
    public void addCatPressed(Event e) {
        addToCatGroup(addCatField);
    }
}
