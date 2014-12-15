package sanara.evaluator;

/**
 * Thrown to indicate an error while evaluating the code using the labs.
 * @author Ahmed Alshakh www.alshakh.net
 */
public class EvaluationError extends Exception {

	/**
	 * Creates a new instance of <code>EvaluationError</code> without detail
	 * message.
	 */
	public EvaluationError() {
	}

	/**
	 * Constructs an instance of <code>EvaluationError</code> with the
	 * specified detail message.
	 *
	 * @param msg the detail message.
	 */
	public EvaluationError(String msg) {
		super(msg);
	}
}
