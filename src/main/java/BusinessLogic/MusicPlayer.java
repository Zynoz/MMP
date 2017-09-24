package BusinessLogic;

import javafx.collections.ObservableList;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import util.Util;

import java.util.Random;

/**
 * This clas is responsible for playing the music.
 */
public final class MusicPlayer {
    private Song currentSong;
    private MusicManager musicManager;
    private Util util;
    private MediaPlayer mediaPlayer;
    private double volume = 100;
    private ObservableList<Song> songs;
    private boolean playPauseStatus = false;

    public void setUtil(Util util) {
        this.util = util;
    }

    public void setSongs(ObservableList<Song> songs) {
        this.songs = songs;
    }

    /**
     * This method plays the specified song.
     * @param song Song to play
     * @return Returns the MediaPlayer object which is playing the song.
     */
    public MediaPlayer playSong(Song song) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            playPauseStatus = false;
        }

        Media media = new Media(song.getSongPath().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
        currentSong = song;
        playPauseStatus = true;
        mediaPlayer.setVolume(volume);
        return mediaPlayer;
    }

    /**
     * This method selects a random song from the play list and calls playSong() with the new random song.
     */
    public void playRandomSong() {
        Song song = songs.get(new Random().nextInt(songs.size()));
        if (currentSong == song) {
            playRandomSong();
        } else {
            playSong(song);
        }
    }

    /**
     * This method returns if the current song is paused or playing.
     * @return
     */
    public boolean playPause() {
        if (playPauseStatus) {
            mediaPlayer.pause();
            playPauseStatus = false;
        } else {
            mediaPlayer.play();
            playPauseStatus = true;
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
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }
    }

    public boolean getPlayPauseStatus() {
        return playPauseStatus;
    }
}