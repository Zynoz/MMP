package controllers;

import com.sun.javafx.css.StyleManager;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import util.Util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {

    private Util util;

    @FXML
    private Label pathLabel;
    @FXML
    private Label pathLabel2;
    @FXML
    private ComboBox themeDropDown;
    @FXML
    private Button showConfig;
    private Stage dialogStage;
    private File selectedDirectory;
    private File secondSelectedDirectory;
    private File themesDir = new File("./src/themes/");
    private String activeThemePath;
    private File[] themesArray = themesDir.listFiles();
    private ObservableList<String> themes = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        themeDropDown.setDisable(true);
        loadThemes();
    }

    @FXML
    public void openDir() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        selectedDirectory = directoryChooser.showDialog(dialogStage);
        if (selectedDirectory != null) {
            pathLabel.setText(selectedDirectory.getAbsolutePath());
        }
    }

    @FXML
    private void openSecondDir() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        secondSelectedDirectory = directoryChooser.showDialog(dialogStage);
        if (secondSelectedDirectory != null) {
            pathLabel2.setText(secondSelectedDirectory.getAbsolutePath());
        }
    }

    @FXML
    private void save() {
        if (selectedDirectory != null) {
            util.setMediaDirectory(selectedDirectory.getAbsolutePath());
        }
        if (secondSelectedDirectory != null) {
            util.setSecondMediaDirectory(secondSelectedDirectory.getAbsolutePath());
        }

        //util.setActiveTheme(themesArray[themeDropDown.getSelectionModel().getSelectedIndex()].getPath());

        util.saveProperties();
        Application.setUserAgentStylesheet(null);
        StyleManager.getInstance().addUserAgentStylesheet(util.getActiveTheme());
        dialogStage.close();
    }

    @FXML
    private void cancel() {
        dialogStage.close();
    }

    @FXML
    private void reset() {
        pathLabel.setText(util.getDefaultMediaDirectory());
        pathLabel2.setText("");
        util.reset();
    }

    private void loadThemes() {


        if (themesArray == null) {
            System.out.println("null");
        } else {
            for (File file : themesArray) {
                String name = file.getName().substring(0, file.getName().length() - 4);
                themes.add(name);
            }
        }

        themeDropDown.setItems(themes);
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setUtil(Util util) {
        this.util = util;
        pathLabel.setText(util.getMediaDirectory());
        pathLabel2.setText(util.getSecondMediaDirectory());
        if (util.getDevOptions()) {
            showConfig.setVisible(true);
        } else {
            showConfig.setVisible(false);
        }
    }

    public void showSettingsFile() {
        try {
            java.awt.Desktop.getDesktop().edit(util.getConfigFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}