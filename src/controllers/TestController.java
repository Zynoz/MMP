package controllers;

import BusinessLogic.MusicManager;
import BusinessLogic.MusicPlayer;
import BusinessLogic.Song;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import util.Tags;
import util.Util;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TestController implements Initializable {

    static final Image DEFAULT_IMAGE = new Image("/resources/default.png");

    private MusicManager musicManager = new MusicManager();
    private ObservableList<Song> songs = FXCollections.observableArrayList();
    private Util util;
    private MusicPlayer musicPlayer = new MusicPlayer();

    @FXML
    private Label songArtistView;
    @FXML
    private Label songNameView;
    @FXML
    private Label songDurationView;
    @FXML
    private Label songCount;
    @FXML
    private TableView<Song> tableView;
    @FXML
    private TextField searchBar;
    @FXML
    private Button playPauseButton;
    @FXML
    private Slider volumeSilder;
    @FXML
    private Slider seekBar;
    @FXML
    private ImageView songCover;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //mediaSeekBar.setDisable(true);
        util = new Util();

        properties();

        //ToDo Not working
        /*
        Application.setUserAgentStylesheet(null);
        StyleManager.getInstance().addUserAgentStylesheet("./src/themes/metroLight.css");
        */
        if (util.getTimesStarted() < 1) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/views/tutorial.fxml"));

            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setTitle("Tutorial");
            stage.setScene(scene);
            Tutorial tutorial = fxmlLoader.getController();
            tutorial.setStage(stage);
            stage.showAndWait();

        }

        musicManager.setUtil(util);
        musicPlayer.setUtil(util);
        musicManager.loadSongs();
        songs = musicManager.getSongs();
        musicPlayer.setSongs(songs);
        songCount.setText(songs.size() + " Songs");
        TableColumn songName = new TableColumn("Songs");
        songName.setCellValueFactory(new PropertyValueFactory<Song, String>("songName"));
        tableView.setItems(songs);
        tableView.getColumns().add(songName);
        displaySong(null);

        setupListeners();
    }

    private void setupListeners() {
        tableView.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> displaySong(newValue)));
        tableView.setOnMouseClicked(ev -> {
            if (ev.getButton() == MouseButton.SECONDARY) {
                //ToDo Context Menu here
            } else if (ev.getButton() == MouseButton.PRIMARY) {
                if (ev.getClickCount() > 1) {
                    playSong(tableView.getSelectionModel().getSelectedItem());
                }
            }
        });

        tableView.setOnKeyPressed(ev -> {
            if (ev.getCode() == KeyCode.ENTER) {
                playSong(tableView.getSelectionModel().getSelectedItem());
            } else if (ev.getCode() == KeyCode.DELETE) {
                Song song = songs.get(tableView.getSelectionModel().getSelectedIndex());
                musicManager.deleteSong(song);
            }
        });

        FilteredList<Song> filteredList = new FilteredList<>(songs, p -> true);
        searchBar.textProperty().addListener(((observable, oldValue, newValue) -> filteredList.setPredicate(song -> {
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }
            String lowerCaseFilter = newValue.toLowerCase();
            return song.getSongName().toLowerCase().contains(lowerCaseFilter);
        })));
        SortedList<Song> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedList);

        volumeSilder.valueProperty().addListener(observable -> musicPlayer.setVolume(volumeSilder.getValue() / 100));

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            util.setTimesStarted(util.getTimesStarted() + 1);
            util.saveProperties();
        }));

//        mediaSeekBar.valueProperty().addListener(observable -> {
//            if (mediaSeekBar.isValueChanging()) {
//                musicManager.seek(musicManager.getMediaPlayer().getTotalDuration().multiply(mediaSeekBar.getValue() / 100.0));
//            }
//        });


    }

    private void properties() {
        File dir = new File(util.getUserDataDirectory());
        dir.mkdir();

        File file = new File(util.getUserDataDirectory() + "config.properties");
        if (!file.exists()) {
            util.createPropertiesFile();
        }
        util.loadProperties();
    }

    private void playSong(Song song) {
        MediaPlayer mediaPlayer = musicPlayer.playSong(song);
        songDurationView.setText(Tags.getDuration(song));
        songArtistView.setText(Tags.getArtist(song));
        System.out.println(Tags.getArtist(song));
        volumeSilder.setValue(mediaPlayer.getVolume() * 100);
        playPauseButton.setText("PAUSE");
        //ToDo only working on first song
        mediaPlayer.setOnPlaying(() -> {
            Thread thread = new Thread(() -> {
                Duration a;
                while (mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    a = mediaPlayer.getCurrentTime();
                    int currentTime = (int) a.toSeconds();
                    System.out.println("current time: " + currentTime);
                    Platform.runLater(() -> seekBar.setValue(currentTime));
                }
            });
            thread.start();
        });
    }

    @FXML
    private void close() {
        System.exit(0);
    }

    @FXML
    private void openMedia() {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(new File(util.getMediaDirectory()));
            } else {
                util.createAlert("Could not open file system", "This feature is not available on all operating systems", Alert.AlertType.ERROR);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void loadMusic() {
        musicManager.loadSongs();
        songCount.setText(songs.size() + " Songs");
    }

    @FXML
    public void editSong() {
        Song song = tableView.getSelectionModel().getSelectedItem();
        musicManager.editSong(song);

    }

    @FXML
    private void about() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/views/about.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setTitle("About");
        stage.setScene(scene);
        stage.setAlwaysOnTop(true);
        stage.showAndWait();
    }

    @FXML
    private void openSettings() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/views/settings.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setTitle("Preferences");
        stage.setScene(scene);
        stage.setAlwaysOnTop(true);
        SettingsController settingsController = fxmlLoader.getController();
        settingsController.setUtil(util);
        settingsController.setDialogStage(stage);
        stage.showAndWait();

        loadMusic();
        songCount.setText(songs.size() + " Songs");
    }

    @FXML
    public void playPause() {
        if (musicPlayer.playPause()) {
            playPauseButton.setText("PAUSE");
        } else {
            playPauseButton.setText("PLAY");
        }
    }

    public void nextSong() {
        musicPlayer.playRandomSong();
        //musicManager.playNextSong();
        Song song = musicPlayer.getPlayingSong();
        playPauseButton.setText("PAUSE");
        displaySong(song);
        volumeSilder.setValue(musicPlayer.getVolume() * 100);
    }

    public void setVolume(double volume) {
        volumeSilder.setValue(volume * 100);
    }

    private void displaySong(Song song) {
        if (song == null) {
            songArtistView.setText("---");
            songNameView.setText("---");
            songDurationView.setText("---");
        } else {
            songNameView.setText(Tags.getTitle(song));
            songDurationView.setText(Tags.getDuration(song));
            if (song.getSongArtist() == null) {
                songArtistView.setText("---");
            } else {
                songArtistView.setText(Tags.getArtist(song));
            }
            if (Tags.getCover(song) != null) {
                Image image = SwingFXUtils.toFXImage(Tags.getCover(song), null);
                songCover.setImage(image);
            } else {
                songCover.setImage(DEFAULT_IMAGE);
                System.out.println("image is null");
            }
        }
    }

    @FXML
    private void deleteSong() {
        musicManager.deleteSong(tableView.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void statistics() {
        //ToDo Statistics
        util.createAlert("Coming soon", "This feature is coming soon", Alert.AlertType.INFORMATION);
    }

    public void help() {

    }
}