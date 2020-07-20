package controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;

public class MessageVC {

    @FXML
    public Label messageField;

    @FXML
    public Button okButton;

    @FXML
    public Button cancelButton;

    public static Stage getMessageStage(String title, String message) throws Exception{
        Stage messageStage = new Stage();
        messageStage.setTitle(title);
        FXMLLoader loader = new FXMLLoader();
        URL url = new File("/home/jackson/Documents/BudgetKeeper/view/Message.fxml").toURI().toURL();
        loader.setLocation(url);
        VBox messageBox = loader.load();
        messageStage.setScene(new Scene(messageBox));
        messageStage.sizeToScene();
        MessageVC messageVC = loader.getController();
        messageVC.setMessage(message);
        return messageStage;
    }

    private void close(){
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void okClicked(Event e) {
        close();
    }

    @FXML
    public void cancelClicked(Event e) {
        close();
    }

    public void setMessage(String message) {
        messageField.setText(message);
    }
}
