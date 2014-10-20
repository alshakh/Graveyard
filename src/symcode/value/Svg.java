/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package symcode.value;

import java.util.Set;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public class Svg implements Value {
	private final String _svg;

	public Svg(String string) {
		_svg = string;
	}

	@Override
	public Set<String> getNeededProperties() {
		return java.util.Collections.EMPTY_SET;
	}


	public String toString(){
		return _svg;
	}

	@Override
	public String toEvaluableScript() {
		return "\""+escapeSvg()+"\"";
	}

	private String escapeSvg(){
		return _svg.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
	}
	
	
}
