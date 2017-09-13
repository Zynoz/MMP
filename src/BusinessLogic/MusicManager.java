package BusinessLogic;

import controllers.EditSong;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import util.Util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

public class MusicManager {

    private ObservableList<Song> songs = FXCollections.observableArrayList();
    private Util util;
    private MediaPlayer mediaPlayer;
    private boolean playPauseStatus;
    private double volume = 100;
    private Song currentSong;

    public void editSong(Song song) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/views/editSong.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setTitle("Edit Song");
        stage.setScene(scene);
        EditSong editSong = fxmlLoader.getController();
        editSong.setSong(song);
        editSong.setDialogStage(stage);
        editSong.setMusicManager(this);
        editSong.setUtil(util);
        stage.showAndWait();

        //not sure
        update();
    }

    public void deleteSong(Song song) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Delete Song: " + song.getSongName());
        alert.setContentText("Do you want to delete this song?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            songs.remove(song);
            File file = new File(song.getSongPath());
            file.delete();
        }
        update();
    }

    private void addSong(Song song) {
        if (song != null) {
            if (!songs.contains(song)) {
                songs.add(song);
            }
        }
        update();
    }

    public void loadSongs() {
        ArrayList<File> listOfSongs = new ArrayList<>();
        File primaryDir = new File(util.getMediaDirectory());
        File secondaryDir = new File(util.getSecondMediaDirectory());

        if(primaryDir.mkdir()) {
        }
        if (secondaryDir.mkdir()) {
        }

        listOfSongs.addAll(Arrays.asList(primaryDir.listFiles()));
        if (secondaryDir.listFiles() != null) {
            listOfSongs.addAll(Arrays.asList(secondaryDir.listFiles()));
        }

        if (!listOfSongs.isEmpty()) {
            songs.clear();
            for (File file : listOfSongs) {
                if (file.isFile()) {
                    if (file.getName().endsWith(".mp3")) {
                        addSong(new Song(file.getName(), Paths.get(file.getPath()).toUri()));
                    }
                }
            }
        }
        update();
    }

    public void playNextSong() {
        playRandomSong(songs.get(generateRandomIndex()));

    }

    public MediaPlayer playSong(Song song) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }
        Media media = new Media(song.getSongPath().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
        currentSong = song;
        mediaPlayer.setOnEndOfMedia(() -> playRandomSong(song));
        playPauseStatus = true;
        mediaPlayer.setVolume(volume);
        return mediaPlayer;
    }

    private void playRandomSong(Song song) {
        int rand = generateRandomIndex();
        Song next = songs.get(rand);
        if (next == song) {
            playRandomSong(song);
        } else {
            playSong(songs.get(rand));
        }
    }

    private int generateRandomIndex() {
        return new Random().nextInt(songs.size());
    }

    public Song getPlayingSong() {
        return currentSong;
    }

    public boolean playPause() {
        if (playPauseStatus) {
            mediaPlayer.pause();
            playPauseStatus = false;
            System.out.println("should be paused");
        } else {
            mediaPlayer.play();
            playPauseStatus = true;
            System.out.println("should be playing");
        }
        return playPauseStatus;
    }

    public void setVolume(double volume) {
        this.volume = volume;
        mediaPlayer.setVolume(volume);
    }

    public double getVolume() {
        return volume;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setUtil(Util util) {
        this.util = util;
    }

    public ObservableList<Song> getSongs() {
        return songs;
    }

    public void setMedia(double v) {

    }

    public void seek(Duration multiply) {
        mediaPlayer.seek(multiply);
    }
}