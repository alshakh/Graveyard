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

    public static void deleteFile(String path) throws IOException {
        File f = new File(path);
        if (f.exists()) {
            Files.delete(f.toPath());
        }
    }

    public static void copyFile(String from, String to) throws IOException {
        Files.copy(new File(from).toPath(), new File(to).toPath());
    }
    
    public static boolean fileExists(String path){
        return new File(path).exists();
    }
}
