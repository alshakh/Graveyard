package mp3organizer.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
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
        if(!createDir(to.getAbsoluteFile().getParentFile())){
            //TODO
        }
        Files.copy(from.toPath(),to.toPath());
    }
    public static void moveFile(File from, File to) throws FileAlreadyExistsException,
                                                           IOException{
        
        if(!createDir(to.getAbsoluteFile().getParentFile())){
            //TODO
        }
        Files.move(from.toPath(), to.toPath());
    }
    public static boolean createDir(File f){
        File dir = f.getAbsoluteFile();
        if(!dir.exists()){
            return dir.getAbsoluteFile().mkdirs();
        }
        return true;
    }
    public static boolean fileExists(File f){
        return f.exists();
    }
    public static void removeFile(File f) throws IOException{
        if(!f.isDirectory()){
            f.delete();
        } else {
            File n[] = f.listFiles();
            for (File cc : n){
                removeFile(cc);
            }
            Files.delete(f.toPath());
        }
    }
    /**
     * check if directory is empty of files not directory .
     * It's a recursive search
     * @param dir
     * @return 
     */
    public static boolean isDirectoryEmpty(File dir){
        // FIXME : can be infinite loop if symbolic link to one of parents.
        //          because it's recursive
        if(!dir.isDirectory()) return false; // not directory -> not empty
        File[] contents = dir.listFiles(); 
        for(File f : contents){
            if(!f.isDirectory()) return false; // there is file inside--> not empty
             // if dir, check inside for non-directory files.
            if(!isDirectoryEmpty(f))return false;
        }
        return true;
    }
}
