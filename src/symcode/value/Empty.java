/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package symcode.value;

import java.util.Collections;
import java.util.Set;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public class Empty implements Value {
	public static final Empty INSTANCE = new Empty();
	private Empty(){
		
	}
	@Override
	public Set<String> getNeededReferences() {
		return Collections.EMPTY_SET;
			}

	@Override
	public String toEvaluableScript() {
		return "0.0";
		// TODO : maybe wanted string instead of zero ???
	}
	
	@Override
	public double toDouble(){
		return 0.0;
	}
}