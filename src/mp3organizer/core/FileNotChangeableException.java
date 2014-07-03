
package mp3organizer.core;

/**
 * Exception for files that with no permissions read and write.
 * @author Ahmed Alshakh <ahmed.s.alshakh@gmail.com>
 */
public class FileNotChangeableException extends Exception {
    /**
     * standard Exception constructor.
     * @param msg 
     */
    public FileNotChangeableException(String msg) {
        super(msg);
    }
    
}
