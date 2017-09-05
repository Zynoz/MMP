package BusinessLogic;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.net.URI;
import java.util.UUID;

public class Song {

    private SimpleStringProperty songName;
    private SimpleStringProperty songArtist;
    private SimpleObjectProperty<UUID> songUuid;
    private SimpleObjectProperty<URI> songPath;

    public Song() {

    }

    public Song(String songName, URI songPath) {
        this.songName = new SimpleStringProperty(songName);
        this.songArtist = new SimpleStringProperty("Test Artist");
        this.songPath = new SimpleObjectProperty<>(songPath);
        songUuid = new SimpleObjectProperty<>(UUID.randomUUID());
    }

    @XmlAttribute
    public UUID getSongUuid() {
        return songUuid.get();
    }

    @XmlElement
    public String getSongName() {
        if (songName.get().endsWith(".mp3")) {
            return songName.get().substring(0, songName.get().length() - 4);
        }
        return songName.get();
    }

    @XmlElement
    public void setSongName(String songName) {
        this.songName.set(songName);
    }

    @XmlElement
    public String getSongArtist() {
        return songArtist.get();
    }

    @XmlElement
    public void setSongArtist(String songArtist) {
        this.songArtist.set(songArtist);
    }

    @XmlElement
    public URI getSongPath() {
        return songPath.get();
    }

    @XmlElement
    public void setSongPath(SimpleObjectProperty<URI> songPath) {
        this.songPath = songPath;
    }

    @Override
    public String toString() {
        return "Song{" +
                "songName='" + songName + '\'' +
                ", songArtist='" + songArtist + '\'' +
                ", songUuid=" + songUuid +
                ", songPath=" + songPath +
                '}';
    }
}