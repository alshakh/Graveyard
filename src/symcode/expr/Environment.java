/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package symcode.expr;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public class Environment {
	private String _environmentStr;

	/**
	 *
	 */
	public Environment(){
		_environmentStr="";
	}

	/**
	 *
	 * @param engine
	 */
	public void apply(ScriptEngine engine){
		try {
			engine.eval(_environmentStr);
		} catch (ScriptException ex) {
			ex.printStackTrace();
		}
	}
	/**
	 * return environment str. For use with combine
	 * @return 
	 */
	private String getEnvStr(){
		return _environmentStr;
	}


	public String resolveRef(String ref){
		ScriptEngine engine = Expression.FACTORY.getEngineByName("JavaScript");
		//
		Object result = null;
		try {
				apply(engine);
			result = engine.eval(ref);
		} catch (ScriptException ex) {
			ex.printStackTrace();
		}
		if (result == null) {
			return "";
		}

		return String.valueOf(result);
	}
	/**
	 *
	 * @param expr
	 */
	public void append(String expr){
		if(expr.trim().isEmpty()) return;
		if(!_environmentStr.isEmpty()) _environmentStr+="\n";
		_environmentStr += expr;
	}
	/**
	 * combine the two environments. otherEnv has priority over this.
	 * otherEnv will be untouched
	 * @param otherEnv
	 */
	public void combine(Environment otherEnv){
		_environmentStr += "\n" + otherEnv.getEnvStr();
	}

	@Override
	public String toString(){
		return _environmentStr;
	}
}
