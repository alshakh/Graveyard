/**    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
     */
    public void setField(FieldKey field, String value) throws ProblemWithAudioFileException {
        for (MediaFile f : files) {
            try {
                f.setField(field, value);
            } catch (Exception ex) {
                throw new ProblemWithAudioFileException(ex.getMessage());
            }
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
