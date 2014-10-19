/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package symcode.expr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public class EnvironmentPropertyList {

	private final ArrayList<Property> _propertyList;
	private final Set<String> _addedReferences; // to avoid infinite loops
	private boolean _circularDependency = false;
	private boolean _allRefsExist = true;

	/**
	 *
	 */
	public EnvironmentPropertyList() {
		_propertyList = new ArrayList<Property>();
		_addedReferences = new HashSet<String>();
	}

	public boolean hasReference(String ref){
		return _addedReferences.contains(ref);
	}
	public void addReference(String ref){
		_addedReferences.add(ref);
	}
	/**
	 *
	 * @param property
	 */
	public void addProperty(Property property) {
		if (!_propertyList.contains(property)) {
			_propertyList.add(property);
		}
		// TODO : better way
		sort();
	}

	public void addPropertySet(Set<Property> properties){
		_propertyList.addAll(properties);
		//
		sort();
	}
	/**
	 *
	 * @return
	 */
	public boolean isValid() {
		return (!_circularDependency && _allRefsExist);
	}

	/**
	 *
	 * @return
	 */
	public boolean isCircularDepedency() {
		return _circularDependency;
	}

	/**
	 *
	 * @return
	 */
	public boolean isAllReferencesExist() {
		return _allRefsExist;
	}

	private int indexOfRefInList(String ref) {
		for (int i = 0; i < _propertyList.size(); i++) {
			if (_propertyList.get(i).getReference().equals(ref)) {
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
		_allRefsExist = true;
		//
		//
		for (int i = 0; i < _propertyList.size(); i++) {
			{ // fix element i 
				Property pty = _propertyList.get(i);
				Set<String> dependsOnSet = pty.getDependencies();
				Set<String> depsCache = new HashSet<String>();// new cache for every element
				while (!dependsOnSet.isEmpty()) {
					int depIdx = -1;
					boolean noDepFixing = true;
					for (String depRef : dependsOnSet) {
						depIdx = indexOfRefInList(depRef);
						// depidx =-1 if non existant
						// which will count as valid
						if (depIdx == -1) {
							_allRefsExist = false;
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
						if (depsCache.contains(_propertyList.get(i).getReference())) {
							System.err.println("Circular dependency");
							_circularDependency = true;
							return;
						} else {
							depsCache.add(_propertyList.get(i).getReference());
							Collections.swap(_propertyList, i, depIdx);
							dependsOnSet = _propertyList.get(i).getDependencies();
						}
					}
				}
			} // END : fix element i
		}
		////
	}

	/**
	 *
	 * @return
	 */
	public Environment toEnvironment() {
		// TODO : do something if invalid 

		Environment env = new Environment();
		/* 
		 objects refs
		 */
		HashSet<String> objRefSet = new HashSet<String>(); // set to ensure no repition
		for (Property p : _propertyList) {
			int dotIdx = p.getReference().indexOf('.');
			if (dotIdx == -1) {
				// it's a const no object needed.
			} else {
				objRefSet.add(p.getReference().substring(0, dotIdx));
			}
		}
		for (String objRef : objRefSet) {
			env.append("var "+ objRef + " = {};");
		}
		for (Property p : _propertyList) {
			env.append(p.toEnvironmentInput());
		}

		return env;
		//
		//
	}

	@Override
	public String toString() {
		StringBuilder a = new StringBuilder();
		for (Property p : _propertyList) {
			a.append(p.toString()).append("\n");
		}
		return a.toString();
	}
}
