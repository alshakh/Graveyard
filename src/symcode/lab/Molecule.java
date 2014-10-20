/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package symcode.lab;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public abstract class Molecule extends Template {

	/**
	 *
	 */
	public static final Set<String> EMPTY_DEPENDENCIES = Collections.unmodifiableSet(new HashSet<String>());
	private final Set<String> _dependencies;
	/**
	 *
	 * @param id
	 * @param version
	 * @param propertySet
	 * @param constsProperties
	 * @param elementsSet
	 * @param deps
	 */
	public Molecule(String id,String version, Set<Property> propertySet, Set<Property> constsProperties, Set<Molecule> elementsSet, Set<String> deps){
		super(id, version, propertySet, constsProperties, elementsSet);
		_dependencies = Collections.unmodifiableSet(deps);
	}

	/**
	 *
	 * @return
	 */
	public Set<String> getDependencies(){
		return _dependencies;
	}

	/**
	 * get the set properties needed to evaluate the molecule.
	 * @return 
	 */
	public Set<Property> getEvaluablePropertySet(){
		Set<Property> propertySet = new HashSet<Property>();
		evaluablePropertySetHelper(propertySet);
		return propertySet;
	}
	private void evaluablePropertySetHelper(Set<Property> propertySet){
		// TODO : you can refer to consts as upper-deep they are but not molecules. molecules can only be referred to inside there compounds
		//+ to avoid infinite adding in case of (valid or invalid) 
		//	circular dependency
		if(_properties.isEmpty() || propertySet.contains(_properties.iterator().next())){
			return;
		}
		
		//-
		//+ adding this.propertySet
		for(Property p : _properties){
			propertySet.add(p);
		}
		//-
		//+ add dependencies
		for(String ref : _dependencies){
			// TODO : on the current implementation constants in 
			//	distant scopes have priority over molecules with
			//	the same name.
			Property constProperty = getConst(ref);
			if(constProperty !=null){
				propertySet.add(constProperty);
				continue;
			}
			// This ref is not a constant
			Molecule m = getMolecule(ref);
			if(m != null) {
			m.evaluablePropertySetHelper(propertySet);
			}
		}
		//-
		//+ Add all childrenelements and consts to list
		for(Molecule m : this.getElements()){
			m.evaluablePropertySetHelper(propertySet);
		}
		for(Property p : this._constProperties){
				propertySet.add(p);
		}
		//-
	}

	

}
