package mp3organizer.core;

import java.io.File;
import java.util.EnumMap;
import org.jaudiotagger.tag.FieldKey;

/**
 *
 * @author Ahmed Alshakh <ahmed.s.alshakh@gmail.com>
 */
public class MediaFile {
    /**
     * data structure to handle the values of the fields
     */
    private EnumMap<FieldKey, String> fieldMap;

    /**
     *
     * @param file File instance of media file
     */
    public MediaFile(File file) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     *
     * @param path String path of file
     */
    public MediaFile(String path) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * move to appropriate path/name
     *
     * @param newPath
     */
    public void setPath(String newPath) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     *
     * @return complete path of file
     */
    public String getPath() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Change value of a field in the file
     *
     * @param field
     * @param value
     */
    public void setField(FieldKey field, String value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * 
     * @param file
     * @return 
     */
    private static EnumMap<FieldKey, String> readFields(MediaFile file) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}