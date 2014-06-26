package mp3organizer.core;

import java.io.File;
import java.util.ArrayList;
import mp3organizer.core.sortpattern.SortPattern;
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
        files = new ArrayList<>();
    }

    public void sort(SortPattern pattern, File rootDir, boolean removeEmptyDirs) {
        for(MediaFile mf:files){
            mf.setFile(pattern.proccessFilePath(mf, rootDir));
        }
        // TODO remove empty folders of files
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
