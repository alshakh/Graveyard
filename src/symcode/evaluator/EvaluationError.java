/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package symcode.evaluator;

/**
 *
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
