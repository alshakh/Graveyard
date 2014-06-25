package mp3organizer.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Mostly static methods to work with files.
 *
 * @author Ahmed Alshakh <ahmed.s.alshakh@gmail.com>
 */
public class FileOperations {

    public static void deleteFile(File f) throws IOException {
        if (f.exists()) {
            Files.delete(f.toPath());
        }
    }

    public static void copyFile(File from, File to) throws IOException {
        Files.copy(from.toPath(),to.toPath());
    }
    
    public static boolean fileExists(File f){
        return f.exists();
    }
}
