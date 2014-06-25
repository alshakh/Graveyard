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

    public static String origTestFile = "testFile.mp3";
    public static String tmpTestFile = "tmpTestFile.mp3";
    //
    public static final EnumMap<FieldKey, String> defaultFields;

    static {
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

    public static File copyTestFile() {
        try {
            FileOperations.copyFile(origTestFile, tmpTestFile);
        } catch (IOException ex) {
            System.out.println("ERROR : Cannot copy " + origTestFile + " : " + ex.getMessage());
        }
        return new File(tmpTestFile);
    }

}
