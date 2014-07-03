
package mp3organizer.core;

/**
 * This exception is a wrapper for many of JAudtioTagger Exceptions.
 * Happens when jAudioFile throws exception.
 * @author Ahmed Alshakh <ahmed.s.alshakh@gmail.com>
 */
public class ProblemWithAudioFileException extends Exception {
    /**
     * Standard exception constructor.
     * @param msg 
     */
    public ProblemWithAudioFileException(String msg){
        super(msg); 
    }
}