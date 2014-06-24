
package mp3organizer.core;

import java.util.ArrayList;

/**
 *
 * @author Ahmed Alshakh <ahmed.s.alshakh@gmail.com>
 */
public class MediaFileList {
    ArrayList<MediaFile> files;
    
    public MediaFileList(){
        files = new ArrayList<MediaFile>();
    }
    
    public void sort(SortPattern pattern){
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
    public void setField(Field field,String value){
        throw new UnsupportedOperationException("Not supported yet.");
    }
}