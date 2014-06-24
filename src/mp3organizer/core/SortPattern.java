
package mp3organizer.core;

import java.util.EnumMap;


/**
 * 
 * @author Ahmed Alshakh <ahmed.s.alshakh@gmail.com>
 */
class SortPattern {
    /**
     * Aliases for fields
     */
    public static final EnumMap<Field,String> ALIASES = 
            new EnumMap<Field,String>(Field.class);
    static {
            ALIASES.put(Field.ARTIST, "<artist>");
            ALIASES.put(Field.YEAR, "<year>");
            ALIASES.put(Field.ALBUM, "<album>");
            ALIASES.put(Field.TITLE, "<title>");
            //TODO the rest of fields;
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