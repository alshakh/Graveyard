/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package symcode.evaluator;

import java.util.HashSet;
import symcode.lab.Lab;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public class Evaluator {

	/**
	 *
	 */
	public final HashSet<Lab> _labs;

	/**
	 *
	 * @param labs
	 */
	public Evaluator(HashSet<Lab> labs){
		_labs = labs;
	}

	/**
	 *
	 * @param lab
	 */
	public Evaluator(Lab lab){
		this(new HashSet<Lab>(java.util.Arrays.asList(new Lab[]{lab})));
	}

	/**
	 *
	 * @param input
	 * @return
	 */
	public Product eval(EvalNode input){
		throw new UnsupportedOperationException();
	}
}
