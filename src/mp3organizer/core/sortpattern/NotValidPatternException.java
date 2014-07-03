
package mp3organizer.core.sortpattern;

/**
 * Exception is throws when attempting to make SortPattern instance with invalid pattern.
 * @author Ahmed Alshakh <ahmed.s.alshakh@gmail.com>
 */
public class NotValidPatternException extends Exception {
    /**
     * Default constructor
     */
    public NotValidPatternException(){
        super();
    }
    /**
     * Constructor
     * @param msg exception message
     */
    public NotValidPatternException(String msg){
        super(msg);
    }
}
