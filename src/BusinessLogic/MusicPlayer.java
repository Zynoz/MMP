package BusinessLogic;

import javafx.scene.media.MediaPlayer;

public class MusicPlayer {
    private Song playingSong;
    private MusicManager musicManager;

    public void playSong(Song song) {
        MediaPlayer mediaPlayer = musicManager.playSong(song);

    }

    public void playNextSong() {

    }

    public boolean playPause() {

        return false;
    }

    public Song getPlayingSong() {
        return playingSong;
    }

}