/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package symcode.evaluator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import sun.org.mozilla.javascript.internal.NativeObject;
import symcode.lab.Property;
import symcode.lab.Property.EvaluableProperty;
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

/**
 * An instance of this class maintains the order of execution of properties in
 * order to get the right order of dependency until converting to actual
 * environment. Mutable object
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
class EnvironmentBuilder {

	private final ArrayList<EvaluableProperty> _propertyList;
	/**
	 * in case isCircular() OR isMissing called before prepare().
	 *if prepare() called first, it will call sort which will put the correct values
	*/
	private boolean _circularDependency = true; 
	private boolean _missingDependency = true;

	public EnvironmentBuilder() {
		_propertyList = new ArrayList<EvaluableProperty>();
	}

	@Override
	public String toString() {
		return toEvaluableScript();
	}
	/**
	 * turning the environment builder into an actual environment. 
	 * @param substituteMissing if true missing references will be substituted 
	 * by empty values.
	 * @return 
	 */
	public Environment toEnvironment(/*boolean substituteMissing*/) throws ScriptException{
		if (! isValid()){
			throw new RuntimeException("Cannot convert non-valid environment");
		}
		return new Environment(toEvaluableScript());
	}

	protected String toEvaluableScript() {
		StringBuilder script = new StringBuilder();
		//+ collect object references
		Set<String> refs = new HashSet<String>();
		for (EvaluableProperty p : _propertyList) {
			if (!p.needsJsObject()) {
				continue;
			}
			refs.add(p.getJsObjectName());
		}
		//-
		//+ add objects to script
		for (String ref : refs) {
			script.append("var ").append(ref).append(" = {}\n");
		}
		//-
		//+ add properties to script
		for (EvaluableProperty p : _propertyList) {
			script.append(constructRefNameOfProperty(p)).append(" = ").append(p.getValue().toEvaluableScript()).append("\n");
		}
		//-

		return script.toString();

	}
	/**
	 * Add property to property list. When adding multiple properties,
	 * <code>addPropertyCollection</code> is recommended. because of fewer
	 * calls of sort();
	 *
	 * @param property
	 */
	public void addProperty(EvaluableProperty property) {
		if (_propertyList.contains(property)) {
			return; // no duplication
		}
		_propertyList.add(property);
	}

	public void addPropertyCollection(Collection<EvaluableProperty> properties) {
		for (EvaluableProperty p : properties) {
			if (_propertyList.contains(p)) {
				continue; // no duplication
			}
			_propertyList.add(p);
		}
	}

	public void prepare(){
		sort();
	}
	/**
	 * must prepare() be called before.
	 * @return 
	 */
	public boolean isValid(){
		if(_circularDependency) return false;
		else if(_missingDependency) return false;
		else return true;
	}
		/**
	 * must prepare() be called before.
	 * @return 
	 */
	public boolean isCirculeDependency(){
		return _circularDependency;
	}
        /**
	 * must prepare() be called before.
	 * @return 
	 */
	public boolean isMissingDependecy(){
		return _missingDependency;
	}

	private int indexOfPropertyInList(String propId) {
		for (int i = 0; i < _propertyList.size(); i++) {
			if (constructRefNameOfProperty(_propertyList.get(i)).equals(propId)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * sort properties such that dependencies come before. If dependency
	 * don't exist, no action.
	 */
	private void sort() {
		/*
		 * The algorithm 
		 * for Item in list (0 --> size)
		 *	if [ all depenencies of Item is before Item ] 
		 * 		It is fine, move on to next
		 *	else 
		 * 		Swap it with the first dependancy after Item. 
		 * 		Check the new Item the same way until it stops on the lowest level of dependency
		 * 		If [ Same Item is fixed twice ] 
		 * 			It's circular dependency
		 */
		// Reset circularDependancy varaible : It will be assigned in here
		_circularDependency = false;
		_missingDependency = false;
		//
		//
		for (int i = 0; i < _propertyList.size(); i++) {
			{ // fix element i 
				EvaluableProperty pty = _propertyList.get(i);
				Set<String> dependsOnSet = pty.getValue().getNeededProperties();
				Set<String> depsCache = new HashSet<String>();// new cache for every element
				while (!dependsOnSet.isEmpty()) {
					int depIdx = -1;
					boolean noDepFixing = true;
					for (String depRef : dependsOnSet) {
						depIdx = indexOfPropertyInList(depRef);
						// depidx =-1 if non existant
						// which will count as valid
						if (depIdx == -1) {
							_missingDependency = true;
						}
						// non existant elements are valid. because no way to fix them
						if (depIdx > i) {
							noDepFixing = false;
							break;
						}
					}
					if (noDepFixing) {
						break;
					} else {
						if (depsCache.contains(constructRefNameOfProperty(_propertyList.get(i)))) {
							System.err.println("Circular dependency");
							_circularDependency = true;
							return;
						} else {
							depsCache.add(constructRefNameOfProperty(_propertyList.get(i)));
							Collections.swap(_propertyList, i, depIdx);
							dependsOnSet = _propertyList.get(i).getValue().getNeededProperties();
						}
					}
				}
			} // END : fix element i
		}
		////
	}
	private String constructRefNameOfProperty(EvaluableProperty p){
		if(!p.needsJsObject()) return p.getPropertyName();
		return p.getJsObjectName() + "."+ p.getPropertyName();
	}

}
