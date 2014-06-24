
package mp3organizer.core;

import java.util.EnumMap;
import org.jaudiotagger.tag.FieldKey;


/**
 * 
 * @author Ahmed Alshakh <ahmed.s.alshakh@gmail.com>
 */
class SortPattern {
    /**
     * Aliases for fields
     */
    public static final EnumMap<FieldKey,String> ALIASES = 
            new EnumMap<FieldKey,String>(FieldKey.class);
    static {
            ALIASES.put(FieldKey.ARTIST, "<artist>");
            ALIASES.put(FieldKey.YEAR, "<year>");
            ALIASES.put(FieldKey.ALBUM, "<album>");
            ALIASES.put(FieldKey.TITLE, "<title>");
    }
   
    
    /**
     * 
     * @param patternStr 
     */
    public SortPattern(String patternStr){
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    /**
     * process original path to new path
     * @param file original file to get the original path/tag from
     * @return 
     */
    public String proccessFilePath(MediaFile file){
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    /**
     * check if sort pattern is valid
     * @param patternStr
     * @return true if valid pattern, false otherwise
     */
    public static boolean isValid(String patternStr){
        throw new UnsupportedOperationException("Not supported yet.");
    }
}