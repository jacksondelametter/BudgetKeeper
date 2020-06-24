package controller;

import javafx.event.Event;
import javafx.fxml.FXML;

public class MainViewController {

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
