package mp3organizer.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.KeyNotFoundException;
import org.jaudiotagger.tag.TagException;

/**
 *
 * @author Ahmed Alshakh <ahmed.s.alshakh@gmail.com>
 */
public class MediaFile {

    private AudioFile audioFile;

    /**
     *
     * @param file File instance of media file
     * @throws java.io.FileNotFoundException
     * @throws org.jaudiotagger.audio.exceptions.CannotReadException
     * @throws org.jaudiotagger.tag.TagException
     * @throws org.jaudiotagger.audio.exceptions.ReadOnlyFileException
     * @throws org.jaudiotagger.audio.exceptions.InvalidAudioFrameException
     */
    public MediaFile(File file) throws CannotReadException,
                                       IOException, TagException,
                                       ReadOnlyFileException,
                                       InvalidAudioFrameException {
        if (!file.exists()) {
            throw new FileNotFoundException("File does not exist");
        }
        audioFile = org.jaudiotagger.audio.AudioFileIO.read(file);
    }

    /**
     *
     * @param path String path of file
     * @throws org.jaudiotagger.audio.exceptions.CannotReadException
     * @throws java.io.IOException
     * @throws org.jaudiotagger.tag.TagException
     * @throws org.jaudiotagger.audio.exceptions.ReadOnlyFileException
     * @throws org.jaudiotagger.audio.exceptions.InvalidAudioFrameException
     */
    public MediaFile(String path) throws CannotReadException,
                                         IOException, TagException,
                                         ReadOnlyFileException,
                                         InvalidAudioFrameException {
        this(new File(path));
    }

    /**
     * move to appropriate File and reopen the file. non-commited changes
     * are discarded.
     *
     * @param newFile
     */
    public void setFile(File newFile) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     *
     * @return
     */
    public File getFile() {
        return audioFile.getFile();
    }

    /**
     * Change value of a field in the file
     *
     * @param field
     * @param value
     */
    public void setField(FieldKey field, String value) throws KeyNotFoundException, FieldDataInvalidException {
        audioFile.getTag().setField(field, value);
    }

    /**
     * get the first instance of the FieldKey.
     * @param field FieldKey 
     * @return value first instance of the FieldKey.
     */
    public String getField(FieldKey field) {
        return audioFile.getTag().getFirst(field);
    }

    /**
     * commit changes and if true resets the values to unchanged in tagMap.
     *
     * @throws org.jaudiotagger.audio.exceptions.CannotWriteException
     */
    public void commit() throws CannotWriteException {
        audioFile.commit();
    }
}
