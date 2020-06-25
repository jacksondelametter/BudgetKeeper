package controller;

import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainViewController {

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

    private void connectToDatabase() {
        String url = "jdbc:sqlite:/home/jackson/Documents/BudgetKeeper/database/BudgetKeeper.db";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            DatabaseMetaData meta = conn.getMetaData();
            System.out.println("Driver name is " + meta.getDriverName());
            String statement = "CREATE TABLE IF NOT EXISTS receipts (amount DOUBLE);";
            String insertStatement = "INSERT INTO receipts (amount) VALUES (100);";
            stmt.execute(statement);
            stmt.execute(insertStatement);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    @FXML
    public void initialize() {
        setupMonthYearChoiceBox();
        setupBudgetPieChart();
        connectToDatabase();
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
