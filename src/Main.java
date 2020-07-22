import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;

public class Main extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Budget Keeper");
        FXMLLoader loader = new FXMLLoader();
        URL url = new File("/home/jackson/Documents/BudgetKeeper/view/MainView.fxml").toURI().toURL();
        loader.setLocation(url);
        VBox vbox = loader.load();
        Scene scene = new Scene(vbox);
        primaryStage.setScene(scene);
        primaryStage.setHeight(1000);
        primaryStage.setWidth(1700);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}