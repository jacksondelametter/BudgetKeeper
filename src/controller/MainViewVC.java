package controller;

import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import model.Category;
import model.Transaction;
import service.Database;

import java.io.File;
import java.net.URL;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

public class MainViewVC {

    @FXML
    public ChoiceBox monthChoiceBox;
    @FXML
    public ChoiceBox yearChoiceBox;
    @FXML
    public PieChart budgetPieChart;
    @FXML
    public VBox totalInformationGroup;
    @FXML
    public VBox incomeInformationGroup;
    @FXML
    public VBox receiptInformationGroup;

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

    private double getTotal(ArrayList<Transaction> trans) {
        double total = 0;
        for(Transaction tran : trans) {
            total += tran.getAmount();
        }
        return total;
    }

    private void setTotalInformation(ArrayList<Transaction> incomeTrans,
                                     ArrayList<Transaction> receiptTrans) {
        double incomeTotal = getTotal(incomeTrans);
        double receiptTotal = getTotal(receiptTrans);
        double totalSaved = incomeTotal - receiptTotal;
        Text incomeText = new Text(String.format("Total Income\n\t-\t%f", incomeTotal));
        Text spentText = new Text(String.format("Total Spent\n\t-\t%f", receiptTotal));
        Text savedText = new Text(String.format("Total Saved\n\t-\t%f", totalSaved));
        totalInformationGroup.getChildren().clear();
        totalInformationGroup.getChildren().addAll(FXCollections.observableArrayList(
                incomeText, spentText, savedText));
    }

    private void setIncomeReceiptInformation(ArrayList<Transaction> trans) {

    }

    private void setupInformationText() {
        Calendar cal = Calendar.getInstance();
        int month = monthChoiceBox.getSelectionModel().getSelectedIndex();
        int year = Integer.parseInt(yearChoiceBox.getValue().toString());
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        Date startDate = new Date(cal.getTime().getTime());
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date endDate = new Date(cal.getTime().getTime());
        ArrayList<Transaction> incomeTrans = Database.getTransactions("Income", startDate, endDate);
        ArrayList<Transaction> receiptTrans = Database.getTransactions("Receipt", startDate, endDate);
        ArrayList<Category> incomeCats = Database.getCategories("Income");
        ArrayList<Category> receiptCats = Database.getCategories("Receipt");
        setTotalInformation(incomeTrans, receiptTrans);
    }

    @FXML
    public void initialize() {
        setupMonthYearChoiceBox();
        setupBudgetPieChart();
        setupInformationText();
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
        setupInformationText();
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
        setupInformationText();
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
        setupInformationText();
    }

    @FXML
    public void printStatsPressed(Event e) {
        System.out.println("Print Stats pressed");
    }
}
