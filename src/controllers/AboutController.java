package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import util.Util;

import java.net.URL;
import java.util.ResourceBundle;

public class AboutController implements Initializable {

    @FXML
    private Label lb1;

    @FXML
    private Label lb2;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lb1.setAlignment(Pos.CENTER);
        lb2.setAlignment(Pos.CENTER);
        lb1.setText("Created by Maximilian Moser");
        lb2.setText("Version " + Util.VERSION);
    }

    public void close(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}