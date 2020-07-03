package controller;

import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Category;
import model.Transaction;
import service.Database;
import service.DateConverter;

import java.io.File;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class MainViewVC {

    @FXML
    public DatePicker datePicker;
    @FXML
    public PieChart budgetPieChart;
    @FXML
    public VBox totalInformationGroup;
    @FXML
    public VBox incomeInformationGroup;
    @FXML
    public VBox receiptInformationGroup;

    private void setupDatePicker() {
        LocalDate rawDate = LocalDate.now();
        datePicker.setValue(rawDate);
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

    private void setIncomeReceiptInformation(VBox infoGroup,
                                             ArrayList<Category> cats,
                                             ArrayList<Transaction> trans) {
        infoGroup.getChildren().clear();
        HashMap<String, VBox> categoryGroupMap = new HashMap<>();
        for(Category cat : cats) {
            if(!categoryGroupMap.containsKey(cat.getName())) {
                VBox categoryGroup = new VBox();
                Text categoryHeader = new Text(String.format("%s\n\t", cat.getName()));
                categoryGroup.getChildren().add(categoryHeader);
                categoryGroupMap.put(cat.getName(), categoryGroup);
            }
        }
        for(Transaction tran : trans) {
            String tranCategory = tran.getCategory();
            Text categoryText = new Text(String.format("\t%-30s - %f",
                    tran.getDescription(), tran.getAmount()));
            categoryGroupMap.get(tranCategory).getChildren().add(categoryText);
        }
        for(Category cat : cats) {
            VBox categoryGroup = categoryGroupMap.get(cat.getName());
            if(categoryGroup.getChildren().size() > 1) {
                Text space = new Text("\n");
                categoryGroup.getChildren().add(space);
                infoGroup.getChildren().add(categoryGroup);
            }
        }
    }

    private void setupInformationText() {
        Date date = Date.valueOf(datePicker.getValue());
        Date startDate = DateConverter.getStartDate(date);
        Date endDate = DateConverter.getEndDate(date);
        ArrayList<Transaction> incomeTrans = Database.getTransactions("Income", startDate, endDate);
        ArrayList<Transaction> receiptTrans = Database.getTransactions("Receipt", startDate, endDate);
        ArrayList<Category> incomeCategories = Database.getCategories("Income");
        ArrayList<Category> receiptCategories = Database.getCategories("Receipt");
        setTotalInformation(incomeTrans, receiptTrans);
        setIncomeReceiptInformation(incomeInformationGroup, incomeCategories, incomeTrans);
        setIncomeReceiptInformation(receiptInformationGroup, receiptCategories, receiptTrans);
    }

    @FXML
    public void initialize() {
        setupDatePicker();
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
    public void dateSelected(Event e) {
        setupBudgetPieChart();
        setupInformationText();
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
