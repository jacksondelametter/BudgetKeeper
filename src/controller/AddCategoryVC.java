package controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import model.Category;
import service.Database;

public class AddCategoryVC {

    @FXML
    public RadioButton incomeRadioButton;

    @FXML
    public RadioButton receiptRadioButton;

    @FXML
    public TextField enterName;

    @FXML
    public Button addButton;

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

    @FXML
    public void addCategoryButtonPressed(Event e) {
        String name = enterName.getText();
        if (!name.equals("")) {
            String type = ((RadioButton) incomeReceiptToggle.getSelectedToggle()).getText();
            Category c = new Category(name, type);
            Database.addCategory(c);
            Stage stage = (Stage) addButton.getScene().getWindow();
            stage.close();
        }
    }
}
