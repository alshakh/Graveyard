package mp3organizer.core;

import java.io.File;
import java.util.ArrayList;
import mp3organizer.core.sortpattern.SortPattern;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.KeyNotFoundException;

/**
 *
 * @author Ahmed Alshakh <ahmed.s.alshakh@gmail.com>
 */
public class MediaFileList {
/**
 * data structure for media files.
 * intialized in constructor
 */
    ArrayList<MediaFile> files;
/**
 * contructor of mediaFileList
 */
    public MediaFileList() {
        files = new ArrayList<>();
    }
    /**
     * Add file to list. mediafile must be intantiated.
     * @param file mediafile object to add
     */
    public void addFile(MediaFile file){
        files.add(file);
    }
        /**
     * move files according to pattern return arraylist of failed files.
     * 
     * @param pattern sort pattern
     * @param rootDir directory to save to
     * @return list of failed files
     */
    public ArrayList<FailedFile> sort(SortPattern pattern, File rootDir) {
        ArrayList<FailedFile> failedFiles = new ArrayList<>();
        for (MediaFile mf : files) {
            try {
                mf.moveFile(pattern.proccessFilePath(mf, rootDir));
            } catch (Exception ex) {
                failedFiles.add(new FailedFile(mf.getFile().getPath(),ex.getMessage()));
            }
        }
        return failedFiles;
    }
    /**
     * get the value of field if it's exactly the same.
     * @param field fieldkey to get value of 
     * @return value of field
     * @throws NotEqualKeyException
     */
    public String getField(FieldKey field) {
        String str = null;
        for (MediaFile f : files) {
            if (str == null) {
                str = f.getField(field);
            } else if (!str.equals(f.getField(field))) {
                return "";
            }
        }
        return str;
    }
    /**
     * setField for all files in the list.
     * @param field Fieldkey of desired field
     * @param value value to be in the field
     * @throws KeyNotFoundException
     * @throws FieldDataInvalidException 
     */
    public void setField(FieldKey field, String value) throws KeyNotFoundException,
                                                              FieldDataInvalidException {
        for (MediaFile f : files) {
            f.setField(field, value);
        }
    }
    /**
     * Commit changed to the full list of all files. 
     * @return 
     */
    public ArrayList<FailedFile> commit(){
        ArrayList<FailedFile> failedFiles = new ArrayList<>();
        
        for (MediaFile mf : files) {
            try {
                mf.commit();
            } catch (Exception ex) {
                failedFiles.add(new FailedFile(mf.getFile().getPath(),ex.getMessage()));
            }
        }
        return failedFiles;
    }
}
