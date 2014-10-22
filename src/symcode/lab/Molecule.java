/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package symcode.lab;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public abstract class Molecule extends Template {

	public static final Set<Property> EMPTY_PROPERTY_SET = Collections.unmodifiableSet(new HashSet<Property>());
	/**
	 *
	 */
	public static final Set<String> EMPTY_DEPENDENCIES = Collections.unmodifiableSet(new HashSet<String>());
	public final Set<String> _dependencies;
	public final Set<Property> _properties; // properties
	/**
	 *
	 * @param id
	 * @param version
	 * @param propertySet
	 * @param constsProperties
	 * @param deps
	 */
	public Molecule(String id,String version, Set<Property> propertySet, Set<Property> constsProperties, Set<String> deps){
		super(id, version, constsProperties);
		_dependencies = Collections.unmodifiableSet(deps);
		//+ properties
		this._properties =Collections.unmodifiableSet(propertySet); 
		//-
	}

	/**
	 * get the set properties needed to evaluate the molecule.
	 * @return 
	 */
	public final Set<Property> getEvaluablePropertySet(){
		Set<Property> propertySet = new HashSet<Property>();
		this.evaluablePropertySet_Helper(propertySet);
		return propertySet;
	}
	
	/**
	 * add required properties to the property set. Must call isThisAddedToPropertySet() before doing anything.
	 * @param propertySet 
	 */
	protected void evaluablePropertySet_Helper(Set<Property> propertySet){
		//+ to avoid infinite adding in case of (valid or invalid) 
		if(propertySet == null) return;
		if(propertySet.contains(_properties.iterator().next())){
			return;
		}
		//-
		//+ properties
		propertySet.addAll(_properties);
		//-
		//+ myConsts
		propertySet.addAll(_constProperties);
		//-
		//+ add dependencies
		for(String ref : _dependencies){
			Molecule m = getMolecule(ref);
			if(m != null) {
				m.evaluablePropertySet_Helper(propertySet);
				continue;
			}
			//
			Property constProperty = getConst(ref);
			if(constProperty !=null){
				propertySet.add(constProperty);
				continue;
			}
		}
		//-
		addClassSpecificPropertySet(propertySet);

	}
	abstract void addClassSpecificPropertySet(Set<Property> propertySet);

	public boolean isSingleAtom() {
		return this.getClass().equals(SingleAtom.class);
	}
}
