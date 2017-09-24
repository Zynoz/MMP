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

/**
 * This class is used for getting/setting the mp3 tags of the songs.
 * This class makes use of the jaudiotagger library.
 */
public class Tags {

    /**
     * This method returns the title of the specified song.
     * @param song Song to get the title from.
     * @return Returns the title of the Song as a String object.
     */
    public static String getTitle(Song song) {
        try {
            AudioFile audioFile = AudioFileIO.read(new File(song.getSongPath()));

            return audioFile.getTag().getFirst(FieldKey.TITLE);

        } catch (IOException | CannotReadException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {
            e.printStackTrace();
        }
        return "fail";
    }

    /**
     * This method sets the title of the specified song.
     * @param song Song to set the title.
     * @param title Title to set for the song.
     */
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

    /**
     * This method returns the duration of the specified song.
     * @param song Song to get the duration from.
     * @return Returns the song duration in seconds.
     */
    public static int getDuration(Song song) {
        try {

            AudioFile audioFile = AudioFileIO.read(new File(song.getSongPath()));
            return (audioFile.getAudioHeader().getTrackLength());

        } catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * This method returns the artist of the specified song.
     * @param song Song to get the artist from.
     * @return Returns the artist of the song as a String object.
     */
    public static String getArtist(Song song) {
        try {

            AudioFile audioFile = AudioFileIO.read(new File(song.getSongPath()));
            return audioFile.getTag().getFirst(FieldKey.ARTIST);

        } catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {
            e.printStackTrace();
        }
        return "fail";
    }

    /**
     * This method sets the artist for the specified song.
     * @param song Song to set the artist.
     * @param artist Artist name to set.
     */
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

    /**
     * This method returns the cover of the specified song.
     * @param song Song to get the image from.
     * @return Returns the image as a BufferedImage object.
     */
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

    /**
     * This method sets the image for the specified song.
     * @param song Song to set the image.
     * @param image Image to set.
     */
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