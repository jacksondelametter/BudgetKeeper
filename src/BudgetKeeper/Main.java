package BudgetKeeper;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.net.URL;

public class Main extends Application{

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Budget Keeper");
        FXMLLoader loader = new FXMLLoader();
        URL url = new File("/home/jackson/Documents/BudgetKeeper/gui/layout.fxml").toURI().toURL();
        loader.setLocation(url);
        VBox vbox = loader.<VBox>load();
        Scene scene = new Scene(vbox);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}