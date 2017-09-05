package BusinessLogic;


import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.TagException;

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
}
