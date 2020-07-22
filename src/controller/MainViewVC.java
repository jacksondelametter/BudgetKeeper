package controller;

import com.sun.source.tree.Tree;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Category;
import model.Transaction;
import service.Database;
import service.DateConverter;

import java.io.File;
import java.net.URL;
import java.sql.Array;
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
    public TreeView infoTreeView;
    @FXML
    private TreeItem root;

    private void setupTreeView() {
        root = new TreeItem("Budget Information");
        root.setExpanded(true);
        infoTreeView.setRoot(root);
    }

    private void setupDatePicker() {
        LocalDate rawDate = LocalDate.now();
        datePicker.setValue(rawDate);
    }

    private void setupBudgetPieChart() {
        budgetPieChart.getData().clear();
        Date date = Date.valueOf(datePicker.getValue());
        Date startDate = DateConverter.getStartDate(date);
        Date endDate = DateConverter.getEndDate(date);
        ArrayList<Transaction> receipts = Database.getTransactions("Receipt", startDate, endDate);
        ArrayList<Category> cats = Database.getCategories("Receipt");
        HashMap<String, Double> categoryTotals = new HashMap<>();
        for(Category cat : cats) {
            if(!categoryTotals.containsKey(cat.getName())) {
                categoryTotals.put(cat.getName(), 0.0);
            }
        }
        for(Transaction receipt : receipts) {
            String categoryName = receipt.getCategory();
            double total = categoryTotals.get(categoryName);
            total += receipt.getAmount();
            categoryTotals.put(categoryName, total);
        }
        for(Category cat : cats) {
            String categoryName = cat.getName();
            double total = categoryTotals.get(categoryName);
            if(total > 0.0) {
                PieChart.Data data = new PieChart.Data(categoryName, total);
                budgetPieChart.getData().add(data);
            }
        }
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
        double saved = incomeTotal - receiptTotal;
        TreeItem totalInfo = new TreeItem("Total");
        TreeItem totalIncome = new TreeItem(String.format("Total Income - %5.2f", incomeTotal));
        TreeItem spentTotal = new TreeItem(String.format("Total Spent - %5.2f", receiptTotal));
        TreeItem totalSaved = new TreeItem(String.format("Total Saved - %5.2f", saved));
        totalInfo.getChildren().add(totalIncome);
        totalInfo.getChildren().add(spentTotal);
        totalInfo.getChildren().add(totalSaved);
        root.getChildren().add(totalInfo);
    }

    private void setIncomeReceiptInformation(String header,
                                             ArrayList<Category> cats,
                                             ArrayList<Transaction> trans) {
        TreeItem incomeReceiptGroup = new TreeItem(header);
        HashMap<String, TreeItem> categoryGroupMap = new HashMap<>();
        for(Category cat : cats) {
            TreeItem catGroup = new TreeItem(cat.getName());
            categoryGroupMap.put(cat.getName(), catGroup);
        }
        for(Transaction tran : trans) {
            String tranCategory = tran.getCategory();
            TreeItem categoryText = new TreeItem(String.format("%s - %.2f",
                    tran.getDescription(), tran.getAmount()));
            categoryGroupMap.get(tranCategory).getChildren().add(categoryText);
        }
        for(Category cat : cats) {
            TreeItem catGroup = categoryGroupMap.get(cat.getName());
            if(catGroup.getChildren().size() > 0) {
                incomeReceiptGroup.getChildren().add(catGroup);
            }
        }
        root.getChildren().add(incomeReceiptGroup);
    }

    private void setupInformationText() {
        Date date = Date.valueOf(datePicker.getValue());
        Date startDate = DateConverter.getStartDate(date);
        Date endDate = DateConverter.getEndDate(date);
        ArrayList<Transaction> incomeTrans = Database.getTransactions("Income", startDate, endDate);
        ArrayList<Transaction> receiptTrans = Database.getTransactions("Receipt", startDate, endDate);
        ArrayList<Category> incomeCategories = Database.getCategories("Income");
        ArrayList<Category> receiptCategories = Database.getCategories("Receipt");
        setupTreeView();
        setTotalInformation(incomeTrans, receiptTrans);
        setIncomeReceiptInformation("Income", incomeCategories, incomeTrans);
        setIncomeReceiptInformation("Receipt", receiptCategories, receiptTrans);
    }

    private void updateMainView() {
        setupBudgetPieChart();
        setupInformationText();
    }

    @FXML
    public void initialize() {
        setupDatePicker();
        updateMainView();
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
        addTransactionStage.setHeight(500);
        addTransactionStage.setWidth(500);
        addTransactionStage.setScene(getScene("AddTransaction.fxml"));
        addTransactionStage.showAndWait();
        updateMainView();
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
        updateMainView();
    }

    @FXML
    public void compareMonthsPressed(Event e) {
        System.out.println("Print Stats pressed");
    }
}
