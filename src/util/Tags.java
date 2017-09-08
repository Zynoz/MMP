package util;


import BusinessLogic.Song;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.images.Artwork;
import org.jaudiotagger.tag.images.ArtworkFactory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Tags {

    public static String getTitle(Song song) {
        try {
            AudioFile audioFile = AudioFileIO.read(new File(song.getSongPath()));

            return audioFile.getTag().getFirst(FieldKey.TITLE);

        } catch (IOException | CannotReadException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {
            e.printStackTrace();
        }
        return "fail";
    }

    public static void setTitle(Song song, String title) {
        try {
            AudioFile audioFile = AudioFileIO.read(new File(song.getSongPath()));
            audioFile.getTag().setField(FieldKey.TITLE, title);
            audioFile.commit();
            System.out.println("changed title to:" + title);
        } catch (IOException | CannotReadException | ReadOnlyFileException | TagException | InvalidAudioFrameException | CannotWriteException e) {
            e.printStackTrace();
        }
    }

    //ToDo convert seconds into minutes and seconds.
    public static String getDuration(Song song) {
        try {

            AudioFile audioFile = AudioFileIO.read(new File(song.getSongPath()));
            return String.valueOf(audioFile.getAudioHeader().getTrackLength());

        } catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {
            e.printStackTrace();
        }
        return "fail";
    }

    public static String getArtist(Song song) {
        try {

            AudioFile audioFile = AudioFileIO.read(new File(song.getSongPath()));
            return audioFile.getTag().getFirst(FieldKey.ARTIST);

        } catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {
            e.printStackTrace();
        }
        return "fail";
    }

    public static void setArtist(Song song, String artist) {
        try {
            AudioFile audioFile = AudioFileIO.read(new File(song.getSongPath()));
            audioFile.getTag().setField(FieldKey.ARTIST, artist);
            audioFile.commit();
            System.out.println("changed artist to: " + artist);
        } catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException | CannotWriteException e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage getCover(Song song) {
        try {
            MP3File mp3File = (MP3File) AudioFileIO.read(new File(song.getSongPath()));
            if (mp3File.getTag() == null) {
                return null;
            }
            Artwork artwork = mp3File.getTag().getFirstArtwork();
            return artwork == null ? null : (BufferedImage) artwork.getImage();
        } catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void setCover(Song song, File image) {
        try {
            AudioFile audioFile = AudioFileIO.read(new File(song.getSongPath()));
            Artwork artwork = ArtworkFactory.createArtworkFromFile(image);
            audioFile.getTag().addField(artwork);
            audioFile.getTag().setField(artwork);
            System.out.println("updated cover");
            audioFile.commit();
        } catch (CannotReadException | IOException | TagException | InvalidAudioFrameException | ReadOnlyFileException | CannotWriteException e) {
            e.printStackTrace();
        }
    }
}