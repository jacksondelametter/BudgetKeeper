package controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class AddTransactionVC {

    @FXML
    public RadioButton incomeRadioButton;

    @FXML
    public RadioButton receiptRadioButton;

    @FXML
    public DatePicker datePicker;

    @FXML
    public TextField enterDescription;

    @FXML
    public TextField enterAmount;

    private ToggleGroup incomeReceiptToggle;

    @FXML
    public void initialize() {
        incomeReceiptToggle = new ToggleGroup();
        incomeRadioButton.setToggleGroup(incomeReceiptToggle);
        receiptRadioButton.setToggleGroup(incomeReceiptToggle);
        incomeRadioButton.fire();
    }

    @FXML
    public void addButtonPressed(Event e) {

    }
}
