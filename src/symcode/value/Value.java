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
public interface Value {
	
	public Set<String> getNeededReferences();

	@Override
	public String toString();
	public double toDouble();

	public String toEvaluableScript();
}
