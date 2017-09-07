package util;


import BusinessLogic.Song;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.images.Artwork;
import org.jaudiotagger.tag.images.ArtworkFactory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Tags {

    public static String getDuration(File file) {
        try {
            MP3File mp3File = (MP3File) AudioFileIO.read(file);
            MP3AudioHeader audioHeader = mp3File.getMP3AudioHeader();
            return audioHeader.getTrackLengthAsString();
        } catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {
            e.printStackTrace();
        }
        return "fail";
    }

    public static String getArtist(File file) {
        try {
            MP3File mp3File = (MP3File) AudioFileIO.read(file);
            if (mp3File.hasID3v2Tag()) {
                AbstractID3v2Tag v2Tag = mp3File.getID3v2Tag();
                return v2Tag.getFirst(ID3v24Frames.FRAME_ID_ARTIST);
            }
        } catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {
            e.printStackTrace();
        }
        return "fail";
    }

    public static void setArtist(File file, String artist) {
        try {
            AudioFile audioFile = AudioFileIO.read(file);
            Tag tag = audioFile.getTag();
            tag.setField(FieldKey.ARTIST, artist);
            audioFile.commit();
            System.out.println("changed artist");
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
            Tag tag = audioFile.getTag();
            tag.addField(artwork);
            tag.setField(artwork);
            audioFile.commit();
        } catch (CannotReadException | IOException | TagException | InvalidAudioFrameException | ReadOnlyFileException | CannotWriteException e) {
            e.printStackTrace();
        }
    }
}