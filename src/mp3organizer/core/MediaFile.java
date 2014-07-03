package mp3organizer.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.KeyNotFoundException;

/**
 *
 * @author Ahmed Alshakh <ahmed.s.alshakh@gmail.com>
 */
public class MediaFile {

    private AudioFile audioFile;

    /**
     * constructor.
     * @param file File instance of media file
     * @throws mp3organizer.core.FileNotChangeableException
     * @throws java.io.FileNotFoundException
     * @throws mp3organizer.core.ProblemWithAudioFileException
            try {
                audioFile= org.jaudiotagger.audio.AudioFileIO.read(file);
            } catch (Exception e) {
                throw new ProblemWithAudioFileException(e.getMessage());
            }
        } else {
            throw new FileNotChangeableException("File is not readable/wriatable");
        }
    }*/
    public MediaFile(File file) throws FileNotChangeableException,
                                       FileNotFoundException,
                                       ProblemWithAudioFileException {
        if (!file.exists()) {
            throw new FileNotFoundException("File does not exist");
        }
        if (file.canRead() && file.canWrite()) {
            try {
                audioFile= org.jaudiotagger.audio.AudioFileIO.read(file);
            } catch (Exception e) {
                throw new ProblemWithAudioFileException(e.getMessage());
            }
        } else {
            throw new FileNotChangeableException("File is not readable/wriatable");
        }
    }

    /**
     * Constructor.
     * @param path String path of file
     * @throws mp3organizer.core.FileNotChangeableException
     * @throws java.io.FileNotFoundException
     * @throws mp3organizer.core.ProblemWithAudioFileException
     */
    public MediaFile(String path) throws FileNotChangeableException,
                                         FileNotFoundException,
                                         ProblemWithAudioFileException {
        this(new File(path));
    }

    /**
     * move to appropriate File and reopen the file. non-commited changes are
     * discarded.
     *
     * @param newFile
     * @throws mp3organizer.core.FileNotChangeableException
     */
    public void moveFile(File newFile) throws FileNotChangeableException {

        try {
            FileOperations.moveFile(audioFile.getFile(), newFile,true);
        } catch (IOException ex) {
            throw new FileNotChangeableException(ex.getMessage());
        }
        audioFile = null;
    }

    /**
     * get file object of mediafile
     *
     * @return file object represent file
     */
    public File getFile() {
        return audioFile.getFile();
    }

    /**
     * Change value of a field in the file
     *
     * @param field
     * @param value
     * @throws org.jaudiotagger.tag.FieldDataInvalidException
     */
    public void setField(FieldKey field, String value) throws KeyNotFoundException, FieldDataInvalidException {
        audioFile.getTag().setField(field, value);
    }

    /**
     * get the first instance of the FieldKey.
     *
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


    /**
     * check if mediafile is connected to actual file in file system.
     * @return true if connected, false otherwise.
     */
    public boolean isGood(){
        return (audioFile!=null);
    }
}
