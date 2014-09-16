/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package symcode.lab;

import java.util.HashMap;
import symcode.expr.Expression;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public interface Evaluable {

	public Expression getBond(Bond b);

	/**
	 *
	 * @param input map<id for input, input product>. when the input is for
	 * an atom(molecule with no elements> the map will have one element ""->
	 * product
	 * @return
	 */
	public Product eval();
	public Product eval(HashMap<String, Product> input);

}
