package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class Tutorial implements Initializable {
    private Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void gotIt() {
        stage.close();
    }
}
