package mp3organizer.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ahmed Alshakh <ahmed.s.alshakh@gmail.com>
 */
public class Constants {

    public static String origTestFile = "testFile.mp3";
    public static String tmpTestFile = "tmpTestFile.mp3";
    
    public static void deleteTmpTestFile(){
        File f = new File(tmpTestFile);
        if (f.exists()) {
            try {
                Files.delete(f.toPath());
            } catch (IOException ex) {
                System.out.println("ERROR : Cannot remove "+tmpTestFile+" : "+ ex.getMessage());
            }
        }
    }
    public static File copyTestFile() {
        File f = new File(tmpTestFile);
        try {
            Files.copy(new File(origTestFile).toPath(), f.toPath());
        } catch (IOException ex) {
            System.out.println("ERROR : Cannot copy "+origTestFile+" : "+ ex.getMessage());
        }
        return f;
    }
}