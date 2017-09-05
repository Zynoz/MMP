package controllers;

import BusinessLogic.MusicManager;
import BusinessLogic.Song;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.commons.io.FilenameUtils;
import util.Util;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class EditSong implements Initializable {

    private Song song;
    private Stage dialogStage;
    private boolean okClicked;
    private Util util;
    private MusicManager musicManager;

    @FXML
    private TextField songNameField;

    @FXML
    private Label artistNameField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setUtil(Util util) {
        this.util = util;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setSong(Song song) {
        if (song == null) {
            songNameField.setText("---");
            artistNameField.setText("---");
        } else {
            this.song = song;
            songNameField.setText(this.song.getSongName());
            artistNameField.setText(this.song.getSongArtist());
        }
    }

    public void setMusicManager(MusicManager musicManager) {
        this.musicManager = musicManager;
    }

    private boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    public void save() {
        if (isInputValid()) {
            File file = new File(song.getSongPath());
            song.setSongName(songNameField.getText());
            file.renameTo(new File(util.getMediaDirectory() + File.separator + songNameField.getText() + "." + FilenameUtils.getExtension(song.getSongPath().toString())));
            okClicked = true;
            musicManager.loadSongs();
            dialogStage.close();
        }
    }

    @FXML
    void cancel(ActionEvent actionEvent) {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String errorMessage = "";
        if (songNameField.getText() == null || songNameField.getText().length() == 0) {
            errorMessage += "No valid song name";
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            util.createAlert("Invalid Fields\n", "Please correct invalid fields", Alert.AlertType.ERROR);
            return false;
        }
    }
}