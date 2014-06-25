package mp3organizer.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.EnumMap;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        File f = new File(tmpTestFile);
        if (f.exists()) {
            try {
                Files.delete(f.toPath());
            } catch (IOException ex) {
                System.out.println("ERROR : Cannot remove " + tmpTestFile + " : " + ex.getMessage());
            }
        }
    }

    public static File copyTestFile() {
        File f = new File(tmpTestFile);
        try {
            Files.copy(new File(origTestFile).toPath(), f.toPath());
        } catch (IOException ex) {
            System.out.println("ERROR : Cannot copy " + origTestFile + " : " + ex.getMessage());
        }
        return f;
    }

}
