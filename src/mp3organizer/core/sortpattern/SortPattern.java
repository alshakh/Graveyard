package mp3organizer.core.sortpattern;

import java.io.File;
import java.util.HashMap;
import mp3organizer.core.FileOperations;
import mp3organizer.core.MediaFile;
import org.jaudiotagger.tag.FieldKey;

/**
 * Handles the patterns for sorting the files. It cannot be changed after
 * instantiating. to change the pattern create new object
 *
 * @author Ahmed Alshakh <ahmed.s.alshakh@gmail.com>
 */
public class SortPattern {

    /**
     * handles the pattern in a String object.
     */
    private String patternStr;

    /**
     * Aliases for fields.
     */
    public static final HashMap< String,FieldKey> ALIASES
            = new HashMap< >();
    public static final String EXTALIAS = ".EXT";
    /**
     * initialize other things.
     */
    static {
        // XXX : must be lowercase
        ALIASES.put("<artist>",FieldKey.ARTIST);
        ALIASES.put("<year>",FieldKey.YEAR);
        ALIASES.put("<album>",FieldKey.ALBUM);
        ALIASES.put("<title>",FieldKey.TITLE);
        //
        
    }

    /**
     * Constructor takes pattern in string to instantiate the object.
     *
     * @param patternStr
     * @throws mp3organizer.core.sortpattern.NotValidPatternException
     */
    public SortPattern(String patternStr) throws NotValidPatternException {
        if(!isValid(patternStr)) throw new NotValidPatternException();
        this.patternStr = patternStr;
    }

    /**
     * process original path to new path,
     *
     * @param mediaFile original file to get the original path/tag from
     * @param rootDir
     * @return
     */
    public File proccessFilePath(MediaFile mediaFile,File rootDir){
        //
        return new File(rootDir.getAbsolutePath()+"/"+getSortPatternPart(mediaFile));
    }
    
    /**
     * replace the part that comes after the rootDir.
     * @param mf mediaFile
     * @return new path after root directory of files
     */
    private String getSortPatternPart(MediaFile mf){
        String newPath = patternStr;
        for(String key : ALIASES.keySet()){
            newPath = newPath.replace(key, mf.getField(ALIASES.get(key)));
        }
        newPath = newPath.replace(EXTALIAS, FileOperations.getExt(mf.getFile()));
        return newPath;
    }

    /**
     * regular expression for testing the validity of pattern.
     * ^((<\w+>)|([\w\d\_\-]))+(\/((<\w+>)|([\w\d\_\-]))+)*(\.EXT)$
     */
    public static String PATTERN_REGEX="^((<\\w+>)|([\\w\\d\\_\\-]))+(\\/((<\\w+>)|([\\w\\d\\_\\-]))+)*(\\.EXT)$";
    
    /**
     * check if sort pattern is valid.
     * @param patternStr
     * @return 
     */
    public static boolean isValid(String patternStr) {
        return patternStr.matches(PATTERN_REGEX);
    }
}
