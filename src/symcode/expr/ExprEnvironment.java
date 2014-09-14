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
	private final String environmentStr;
	//
	public ExprEnvironment(String environmentStr){
		this.environmentStr = environmentStr;
	}
	//
	public void apply(ScriptEngine engine){
		try {
			engine.eval(environmentStr);
		} catch (ScriptException ex) {
			ex.printStackTrace();
		}
	}
	//
	private String getEnvStr(){
		return environmentStr;
	}
	//
	public static ExprEnvironment combine(ExprEnvironment envA,ExprEnvironment envB){
		return new ExprEnvironment(envA.getEnvStr() + "\n" + envB.getEnvStr());
	}
}
