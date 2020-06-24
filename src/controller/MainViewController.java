package controller;

import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainViewController {

    @FXML
    public ChoiceBox monthChoiceBox;
    @FXML
    public ChoiceBox yearChoiceBox;

    private void setupMonthYearChoiceBox() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MMM");
        LocalDateTime rawDate = LocalDateTime.now();
        String formattedDate = formatter.format(rawDate);
        String[] splitDate = formattedDate.split("/");
        String currentMonth = splitDate[1];
        int currentYear = Integer.parseInt(splitDate[0]);
        monthChoiceBox.setItems(FXCollections.observableArrayList("Jan", "Feb", "Mar",
                "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"));
        monthChoiceBox.setValue(currentMonth);
        int startYear = 2019;
        for(int i=startYear;i<=currentYear;i++) {
            yearChoiceBox.getItems().add(i);
        }
        yearChoiceBox.setValue(currentYear);
    }

    @FXML
    public void initialize() {
        setupMonthYearChoiceBox();
    }

    @FXML
    public void addIncomePressed(Event e){
        System.out.println("Add Income Pressed");
    }

    @FXML
    public void addReceiptPressed(Event e){
        System.out.println("Add Receipt Pressed");
    }

    @FXML
    public void addCategoryPressed(Event e){
        System.out.println("Add Category Pressed");
    }

    @FXML
    public void deleteIncomePressed(Event e) {
        System.out.println("Delete Income Pressed");
    }

    @FXML
    public void deleteReceiptPressed(Event e) {
        System.out.println("Delete Receipt Pressed");
    }

    @FXML
    public void deleteCategoryPressed(Event e) {
        System.out.println("Delete Category Pressed");
    }

    @FXML
    public void printStatsPressed(Event e) {
        System.out.println("Print Stats pressed");
    }
}
