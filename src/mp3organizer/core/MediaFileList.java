package mp3organizer.core;

import java.util.ArrayList;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.KeyNotFoundException;

/**
 *
 * @author Ahmed Alshakh <ahmed.s.alshakh@gmail.com>
 */
public class MediaFileList {

    ArrayList<MediaFile> files;

    public MediaFileList() {
        files = new ArrayList<MediaFile>();
    }

    public void sort(SortPattern pattern) {
        throw new UnsupportedOperationException("Not supported yet.");
        //TODO
    }

    public String getField(FieldKey field) throws NotEqualKeyException {
        String str = null;
        for (MediaFile f : files) {
            if (str == null) {
                str = f.getField(field);
            } else if (!str.equals(f.getField(field))) {
                throw new NotEqualKeyException();
            }
        }
        return str;
    }

    public void setField(FieldKey field, String value) throws KeyNotFoundException,
                                                              FieldDataInvalidException {
        for (MediaFile f : files) {
            f.setField(field, value);
        }
    }

    public void commit() throws CannotWriteException {
        for(MediaFile f:files){
            f.commit();
        }
    }
}
