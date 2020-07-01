package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Category;
import service.Database;

import java.util.ArrayList;

public class DeleteCategoryVC {

    @FXML
    public RadioButton incomeRadioButton;

    @FXML
    public RadioButton receiptRadioButton;

    @FXML
    public TableView categoryTableView;

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

    private void populateCategoryTableView(ArrayList<Category> cats) {
        categoryTableView.getColumns().clear();
        categoryTableView.getItems().clear();
        TableColumn<String, Category> nameCol = new TableColumn<>("Category");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryTableView.getColumns().add(nameCol);
        for (Category cat : cats) {
            categoryTableView.getItems().add(cat);
        }
        categoryTableView.setRowFactory(tableView -> {
            TableRow<Category> row = new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {
                if(mouseEvent.getClickCount() == 2 && !row.isEmpty()) {
                    Category cat = row.getItem();
                    System.out.printf("Selected %s", cat.getName());
                }
            });
            return row;
        });
    }

    @FXML
    public void incomeRadioButtonPressed(Event e) {
        ArrayList<Category> incomeCategory = Database.getCategories("Income");
        populateCategoryTableView(incomeCategory);
    }

    @FXML
    public void receiptRadioButtonPressed(Event e) {
        ArrayList<Category> receiptCategories = Database.getCategories("Receipt");
        populateCategoryTableView(receiptCategories);
    }
}
