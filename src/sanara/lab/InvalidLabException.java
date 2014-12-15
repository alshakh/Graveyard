package sanara.lab;

/**
 *
 * @author Ahmed Alshakh <ahmed.s.alshakh@gmail.com>
 */
public class InvalidLabException extends Exception {

	/**
	 * Creates a new instance of <code>InvalidLabException</code> without
	 * detail message.
	 */
	public InvalidLabException() {
	}

	/**
	 * Constructs an instance of <code>InvalidLabException</code> with the
	 * specified detail message.
	 *
	 * @param msg the detail message.
	 */
	public InvalidLabException(String msg) {
		super(msg);
	}
}
