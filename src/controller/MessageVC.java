package controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MessageVC {

    @FXML
    public Label messageField;

    @FXML
    public Button okButton;

    @FXML
    public Button cancelButton;

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
