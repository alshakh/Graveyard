package mp3organizer;

import java.io.File;
import mp3organizer.core.FileOperations;

/**
 *
 * @author Ahmed Alshakh <ahmed.s.alshakh@gmail.com>
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println(FileOperations.findEmptyFile(new File("testFile.mp3")));
    }
    
}
