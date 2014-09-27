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
public class ExprEnvironment {
	private String _environmentStr;
	//
	public ExprEnvironment(){
		_environmentStr="";
	}
	//
	public void apply(ScriptEngine engine){
		try {
			engine.eval(_environmentStr);
		} catch (ScriptException ex) {
			ex.printStackTrace();
		}
	}
	//
	private String getEnvStr(){
		return _environmentStr;
	}
	//
	/**
	 * combine the two environments. otherEnv has priority over this.
	 * otherEnv will be untouched
	 * @param otherEnv
	 */
	public void combine(ExprEnvironment otherEnv){
		_environmentStr += "\n" + otherEnv.getEnvStr();
	}
}
