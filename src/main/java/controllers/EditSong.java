package controllers;

import BusinessLogic.MusicManager;
import BusinessLogic.Song;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import util.Tags;
import util.Util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class is responsible for editing songs.
 */
public class EditSong implements Initializable {

    private Song song;
    private Stage dialogStage;
    private boolean okClicked;
    private Util util;
    private MusicManager musicManager;
    private File selectedFile;
    private Image image;

    @FXML
    private TextField songNameField;
    @FXML
    private TextField songArtistField;
    @FXML
    private ImageView artCover;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setUtil(Util util) {
        this.util = util;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * This method sets songNameField, songArtistField and artCover.
     * @param song Song to edit.
     */
    public void setSong(Song song) {
        if (song == null) {
            songNameField.setText("---");
            songArtistField.setText("---");
        } else {
            this.song = song;
            songNameField.setText(Tags.getTitle(song));
            songArtistField.setText(Tags.getArtist(song));
            if (Tags.getCover(song) != null) {
                Image image = SwingFXUtils.toFXImage(Tags.getCover(song), null);
                artCover.setImage(image);
            } else {
                System.out.println("no image");
                artCover.setImage(TestController.DEFAULT_IMAGE);
            }
        }
    }

    public void setMusicManager(MusicManager musicManager) {
        this.musicManager = musicManager;
    }

    private boolean isOkClicked() {
        return okClicked;
    }

    /**
     * This method is called when the user clicks on the art cover.
     * It opens a file chooser where the user can select a jpg/png file as the art cover for the song.
     */
    @FXML
    public void openDir() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose song cover");
        FileChooser.ExtensionFilter extensionFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.jpg");
        FileChooser.ExtensionFilter extensionFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
        fileChooser.getExtensionFilters().addAll(extensionFilterJPG, extensionFilterPNG);
        selectedFile = fileChooser.showOpenDialog(dialogStage);
        if (selectedFile != null) {
            try {
                BufferedImage bufferedImage = ImageIO.read(selectedFile);
                image = SwingFXUtils.toFXImage(bufferedImage, null);
                artCover.setImage(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method saves all changes.
     */
    @FXML
    public void save() {
        if (isInputValid()) {
            //File file = new File(song.getSongPath());
            //file.renameTo(new File(util.getMediaDirectory() + File.separator + songNameField.getText() + "." + FilenameUtils.getExtension(song.getSongPath().toString())));
            //song.setSongName(songNameField.getText());
            Tags.setTitle(song, songNameField.getText());
            Tags.setArtist(song, songArtistField.getText());
            if (selectedFile != null) {
                Tags.setCover(song, selectedFile);
            }
            okClicked = true;
            musicManager.loadSongs();
            dialogStage.close();
        }
    }

    @FXML
    void cancel() {
        dialogStage.close();
    }

    /**
     * This method checks if the changed fields for the song are valid.
     * @return Returns true when the input is valid.
     */
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