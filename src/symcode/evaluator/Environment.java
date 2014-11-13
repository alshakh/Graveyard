/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package symcode.evaluator;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import sun.org.mozilla.javascript.internal.NativeObject;
import symcode.lab.Property.ProductProperty;
import symcode.value.*;

/**
 * Environment manages the execution of properties to get the result. It closely
 * related to value.Expr class.
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public class Environment {

	private final ScriptEngine _engine;
	private static final ScriptEngineManager FACTORY = new ScriptEngineManager();

	//
	public Environment(String initScript) throws ScriptException {
		_engine = FACTORY.getEngineByName("JavaScript");
		_engine.eval(initScript);
	}

	public Value resolveReference(String ref) {
		return eval(new Expr(ref));
	}

	public Value eval(Value value) {
		//
		Object result = null;
		try {
			// evaluate JavaScript code from String
			result = _engine.eval(value.toEvaluableScript());
		} catch (ScriptException ex) {
			ex.printStackTrace();
		}
		//
		return objectToValue(result);
	}
	private static Value objectToValue(Object obj){
		if (obj == null) {
			return Empty.INSTANCE;
		}
		if (obj.getClass().equals(Double.class)) {
			return new Doub(obj.toString());
		}
		return new Str(obj.toString());
	}
	/**
	 * returns a full evaluated properties as product properties.
	 * @param id
	 * @return 
	 */
	public Set<ProductProperty> evalMolecule(String id) throws ScriptException {
		Set<ProductProperty> properties = new HashSet<ProductProperty>();
		Iterator<Map.Entry<Object, Object>> itr = ((NativeObject)_engine.eval(id)).entrySet().iterator();
		while(itr.hasNext()){
			Map.Entry entry = itr.next();
			properties.add(new ProductProperty(entry.getKey().toString(), objectToValue(entry.getValue())));
		}
		return properties;
	}
}

