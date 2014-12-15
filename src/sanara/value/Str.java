/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sanara.value;

import java.util.Set;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public class Str implements Value{
	private final String _str;

	public Str(String str) {
		_str = str;
	}

	@Override
	public Set<String> getNeededReferences() {
		return java.util.Collections.EMPTY_SET;
	}

	@Override
	public String toEvaluableScript() {
		return "\""+escapeString(_str)+"\"";
	}

	public static String escapeString(String str){
		return str.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
	}

	public String toString(){
		return _str;
	}

	@Override
	public double toDouble(){
		return 0.0;
	}
}
