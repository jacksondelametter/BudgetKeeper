package controller;

import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainViewVC {

    @FXML
    public ChoiceBox monthChoiceBox;
    @FXML
    public ChoiceBox yearChoiceBox;
    @FXML
    public PieChart budgetPieChart;
    @FXML
    public TableView budgetTableView;

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

    private void setupBudgetPieChart() {
        PieChart.Data d1 = new PieChart.Data("Utillities", 200);
        PieChart.Data d2 = new PieChart.Data("Subscriptions", 800);

        budgetPieChart.getData().add(d1);
        budgetPieChart.getData().add(d2);
    }

    private void setupBudgetTableView() {

    }

    @FXML
    public void initialize() {
        setupMonthYearChoiceBox();
        setupBudgetPieChart();
    }

    private Scene getScene(String fxml) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL url = new File("/home/jackson/Documents/BudgetKeeper/view/" + fxml).toURI().toURL();
            VBox vbox = loader.load(url);
            return new Scene(vbox);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return null;
    }

    @FXML
    public void addTransactionPressed(Event e) throws Exception{
        Stage addTransactionStage = new Stage();
        addTransactionStage.setTitle("Add Transaction");
        addTransactionStage.setX(500);
        addTransactionStage.setY(500);
        addTransactionStage.setHeight(300);
        addTransactionStage.setWidth(500);
        addTransactionStage.setScene(getScene("AddTransaction.fxml"));
        addTransactionStage.showAndWait();
    }

    @FXML
    public void addCategoryPressed(Event e) throws Exception{
        Stage addCategoryStage = new Stage();
        addCategoryStage.setX(500);
        addCategoryStage.setY(500);
        addCategoryStage.setHeight(300);
        addCategoryStage.setWidth(500);
        addCategoryStage.setTitle("Add Category");
        addCategoryStage.setScene(getScene("AddCategory.fxml"));
        addCategoryStage.showAndWait();
    }

    @FXML
    public void deleteTransactionPressed(Event e) throws Exception {
        Stage deleteTransactionPressed = new Stage();
        deleteTransactionPressed.setX(500);
        deleteTransactionPressed.setY(500);
        deleteTransactionPressed.setHeight(300);
        deleteTransactionPressed.setWidth(500);
        deleteTransactionPressed.setTitle("Add Category");
        deleteTransactionPressed.setScene(getScene("DeleteTransaction.fxml"));
        deleteTransactionPressed.showAndWait();
    }

    @FXML
    public void deleteCategoryPressed(Event e) throws Exception {
        Stage deleteCategoryPressed = new Stage();
        deleteCategoryPressed.setX(500);
        deleteCategoryPressed.setY(500);
        deleteCategoryPressed.setHeight(300);
        deleteCategoryPressed.setWidth(500);
        deleteCategoryPressed.setTitle("Delete Category");
        deleteCategoryPressed.setScene(getScene("DeleteCategory.fxml"));
        deleteCategoryPressed.showAndWait();
    }

    @FXML
    public void printStatsPressed(Event e) {
        System.out.println("Print Stats pressed");
    }
}
