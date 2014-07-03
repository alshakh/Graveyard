
package mp3organizer.core.sortpattern;

/**
 *
 * @author Ahmed Alshakh <ahmed.s.alshakh@gmail.com>
 */
class NotValidPatternException extends Exception {
    public NotValidPatternException(){
        super();
    }
    public NotValidPatternException(String msg){
        super(msg);
    }
}
