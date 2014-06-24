
package mp3organizer.core;

import java.util.ArrayList;
import org.jaudiotagger.tag.FieldKey;

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
    
    public String getField(FieldKey field){
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void setField(FieldKey field,String value){
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void commit(){
        throw new UnsupportedOperationException("Not supported yet.");
    }
}