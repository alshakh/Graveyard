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
import java.util.Set;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import symcode.lab.Property;
import symcode.value.*;

/**
 * Environment manages the execution of properties to get the result. It closely
 * related to value.Expr class.
 * @author Ahmed Alshakh www.alshakh.net
 */
public class Environment {
	// Values to be return by inspect
	public static final int VALID = 1;
	public static final int CIRCULAR_DEPENDENCY = 2;
	public static final int MISSING_DEPENDENCY = 3;
	//
	private static final ScriptEngineManager FACTORY = new ScriptEngineManager();

	private final PropertyOrderList _propertyOrderList;

	public Environment(){
		_propertyOrderList = new PropertyOrderList();
		
	}

	public Value resolveReference(String ref){
		return eval(new Expr(ref));
	}
	public Value eval(Value value) {
		// non-expressions are needed to evaluate.
		if(! value.getClass().equals(Expr.class)) return value;
		//
		if(inspect()!=VALID) return new Doub("0");
		// 
		ScriptEngine engine = FACTORY.getEngineByName("JavaScript");
		//
		Object result = null;
		try {
			engine.eval(_propertyOrderList.toEvaluableScript());
			// evaluate JavaScript code from String
			result = engine.eval(value.toEvaluableScript());
		} catch (ScriptException ex) {
			ex.printStackTrace();
		}
		//
		if (result == null) {
			return new Doub("0");
		}
		if (result.getClass().equals(Double.class)){
			return new Doub(result.toString());
		}
		return new Str(result.toString());
	}

	public void addPropertyCollection(Collection<Property> pc){
		_propertyOrderList.addPropertyCollection(pc);
	}
	public void addProperty(Property property){
		_propertyOrderList.addProperty(property);
	}

	public int inspect(){
		return _propertyOrderList.inspect();
	}

}
/**
 * An instance of this class maintains the order of execution of properties in
 * order to get the right order of dependency. This class only interacted with 
 by Environment and nothing else.
 * @author Ahmed Alshakh www.alshakh.net
 */
class PropertyOrderList {
	private final ArrayList<Property> _propertyList;	
	private boolean _circularDependency = false;
	private boolean _missingDependency = false;

	public PropertyOrderList() {
		_propertyList = new ArrayList<Property>();
	}

	protected String toEvaluableScript(){
		StringBuilder script = new StringBuilder();
		//+ collect object references
		Set<String> refs = new HashSet<String>();
		for(Property p : _propertyList){
			if(!p.isObjectMember()) continue;
			refs.add(p.getObjectReference());
		}
		//-
		//+ add objects to script
		for(String ref : refs){
			script.append("var ").append(ref).append(" ={}\n");
		}
		//-
		//+ add properties to script
		for(Property p : _propertyList){
			script.append(p._id).append(" = ").append(p._value.toEvaluableScript()).append("\n");
		}
		//-

		return script.toString();

	}

	/**
	 * Add property to property list. When adding multiple properties,
	 * addPropertyCollection is recommended. because of fewer calls of sort();
	 * @param property 
	 */
	public void addProperty(Property property){
		if(_propertyList.contains(property)) return; // no duplication
		_propertyList.add(property);
		//
		sort();
	}
        public void addPropertyCollection(Collection<Property> properties){
		for(Property p : properties){
			if(_propertyList.contains(p)) continue; // no duplication
			_propertyList.add(p);
		}
		//
		sort();
	}

	protected int inspect(){
		if(_circularDependency) return Environment.CIRCULAR_DEPENDENCY;
		if(_missingDependency) return Environment.MISSING_DEPENDENCY;
		return Environment.VALID;
	}

	private int indexOfPropertyInList(String propId) {
		for (int i = 0; i < _propertyList.size(); i++) {
			if (_propertyList.get(i)._id.equals(propId)) {
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
				Property pty = _propertyList.get(i);
				Set<String> dependsOnSet = pty._value.getNeededProperties();
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
						if (depsCache.contains(_propertyList.get(i)._id)) {
							System.err.println("Circular dependency");
							_circularDependency = true;
							return;
						} else {
							depsCache.add(_propertyList.get(i)._id);
							Collections.swap(_propertyList, i, depIdx);
							dependsOnSet = _propertyList.get(i)._value.getNeededProperties();
						}
					}
				}
			} // END : fix element i
		}
		////
	}

}