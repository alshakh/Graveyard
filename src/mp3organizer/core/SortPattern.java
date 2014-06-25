package mp3organizer.core;

import java.util.EnumMap;
import org.jaudiotagger.tag.FieldKey;

/**
 * Handles the patterns for sorting the files. It cannot be changed after
 * instantiating. to change the pattern create new object
 *
 * @author Ahmed Alshakh <ahmed.s.alshakh@gmail.com>
 */
public class SortPattern {

    /**
     * handles the pattern in a String object
     */
    private String patternStr;

    /**
     * Aliases for fields
     */
    public static final EnumMap<FieldKey, String> ALIASES
            = new EnumMap<FieldKey, String>(FieldKey.class);

    static {
        ALIASES.put(FieldKey.ARTIST, "<artist>");
        ALIASES.put(FieldKey.YEAR, "<year>");
        ALIASES.put(FieldKey.ALBUM, "<album>");
        ALIASES.put(FieldKey.TITLE, "<title>");
        //
    }

    /**
     * Constructor takes pattern in string to instantiate the object.
     *
     * @param patternStr
     */
    public SortPattern(String patternStr) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * process original path to new path,
     *
     * @param file original file to get the original path/tag from
     * @return
     */
    public String proccessFilePath(MediaFile file) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * cleans trailing spaces and double slashes.
     */
    public void clean() {
        throw new UnsupportedOperationException("Not supported yet.");
//        if (!isValid(patternStr)) {
//            return;
//        }
    }

    /**
     * check if sort pattern is valid.
     *
     * @param patternStr
     * @return true if valid pattern, false otherwise
     */
        //^((<\w+>)|([\w\d\_\-]))+(\/((<\w+>)|([\w\d\_\-]))+)*(\.EXT)$
    public static String PATTERN_REGEX="^((<\\w+>)|([\\w\\d\\_\\-]))+(\\/((<\\w+>)|([\\w\\d\\_\\-]))+)*(\\.EXT)$";
    public static boolean isValid(String patternStr) {
        return patternStr.matches(PATTERN_REGEX);
    }
}
