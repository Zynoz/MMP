package BusinessLogic;

import javafx.collections.ObservableList;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import util.Util;

import java.util.Random;

public class MusicPlayer {
    private Song currentSong;
    private MusicManager musicManager;
    private Util util;
    private MediaPlayer mediaPlayer;
    private double volume = 100;
    private ObservableList<Song> songs;

    public void setSongs(ObservableList<Song> songs) {
        this.songs = songs;
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
        mediaPlayer.setVolume(volume);
        return mediaPlayer;
    }

    public void playNextSong() {

    }

    public void playRandomSong() {
        Song song = songs.get(new Random().nextInt(songs.size()));
        playSong(song);
    }

    public boolean playPause() {

        return false;
    }

    public Song getPlayingSong() {
        return currentSong;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }
}