/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package symcode.expr;
import javax.script.*;
/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public class Expression {
	// factory just creates new engines. no need to make many of them.
	private static final ScriptEngineManager factory = new ScriptEngineManager();
	//
	private final String _exprStr;
	//
	public Expression(String expr){
		_exprStr = expr;
	} 
	//
	public Expression(){
		_exprStr = "";
	}
	//
	public double eval(ExprEnvironment environment){
		// 
		ScriptEngine engine = factory.getEngineByName("JavaScript");
		//
		Object result = null;
		try {
			if (environment != null)
				environment.apply(engine);
			// evaluate JavaScript code from String
			result = engine.eval(_exprStr);
		} catch (ScriptException ex) {
			ex.printStackTrace();
		}
		if( result == null ) return 0.0;
		if(! result.getClass().equals(Double.class)) return 0.0;
		return (Double)result;
	}
	//
	public Double eval(){
		return eval(null);
	}
	//
	@Override
	public String toString(){
		return _exprStr;
	}
}
