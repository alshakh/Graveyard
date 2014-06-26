package mp3organizer.core;

import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import org.jaudiotagger.tag.FieldKey;

/**
 *
 * @author Ahmed Alshakh <ahmed.s.alshakh@gmail.com>
 */
public class Constants {

    public static File origTestFile;
    public static File tmpTestFile;
    //
    public static File TestTmpFolder;
    //
    public static final EnumMap<FieldKey, String> defaultFields;

    static {
        origTestFile = new File("testFile.mp3");
        tmpTestFile = new File("tmpTestFile.mp3");
        TestTmpFolder = new File("tmp");
        //
        defaultFields = new EnumMap<>(FieldKey.class);
        defaultFields.put(FieldKey.ARTIST, "artistTest");
        defaultFields.put(FieldKey.TITLE, "titleTest");
        defaultFields.put(FieldKey.ALBUM, "albumTest");
        defaultFields.put(FieldKey.YEAR, "1111");
    }

    //

    public static void deleteTmpTestFile() {
        try {
            FileOperations.deleteFile(tmpTestFile);
        } catch (IOException ex) {
            System.out.println("ERROR : Cannot remove " + tmpTestFile + " : " + ex.getMessage());
        }
    }

    public static void copyTestFile() {
//        try {
//            FileOperations.copyFile(origTestFile, tmpTestFile);
//        } catch (IOException ex) {
//            System.out.println("ERROR : Cannot copy " + origTestFile + " : " + ex.getMessage());
//        }
    }
    
    public static String getRandomWord(){
        StringBuilder str = new StringBuilder();
        for(int i=0 ; i<7 ; i++){
            str.append((char)('a'+Math.random()*('z'-'a')));
        }
        return str.toString();
    }
}
