package BusinessLogic;

import javafx.collections.ObservableList;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import util.Util;

import java.util.Random;

public final class MusicPlayer {
    private Song currentSong;
    private MusicManager musicManager;
    private Util util;
    private MediaPlayer mediaPlayer;
    private double volume = 100;
    private ObservableList<Song> songs;
    private boolean playPauseStatus;

    public void setUtil(Util util) {
        this.util = util;
    }

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

    public void playRandomSong() {
        Song song = songs.get(new Random().nextInt(songs.size()));
        playSong(song);
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

    public Song getPlayingSong() {
        return currentSong;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
        mediaPlayer.setVolume(volume);
    }
}