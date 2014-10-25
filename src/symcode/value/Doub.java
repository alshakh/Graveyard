/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package symcode.value;

import java.util.Set;

/**
 * Just like Double
 * @author Ahmed Alshakh www.alshakh.net
 */
public class Doub implements Value{
	private final double _value;

	public Doub(String string) {
		double n = 0.0;
		try {
			n = Double.valueOf(string);
		} catch (NumberFormatException e) {
		}
		_value = n;
	}

	@Override
	public Set<String> getNeededProperties() {
		return java.util.Collections.EMPTY_SET;
	}

	public String toString(){
		return String.valueOf(_value);
	}

	@Override
	public String toEvaluableScript() {
		return String.valueOf(_value);
	}

	public double toDouble() {
		return _value;
	}
	
	
}
