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
import symcode.expr.EnvironmentPropertyList;
import symcode.expr.Expression;
import symcode.expr.Property;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public abstract class Molecule extends Template implements Loadable  {

	/**
	 *
	 */
	public static final Set<String> EMPTY_REFERENCES = new HashSet<String>();
	public static final Set<Property> EMPTY_PROPERTIES = new HashSet<Property>();
	private final Set<Property> _properties;
	private final Set<String> _references;
	/**
	 *
	 * @param id
	 * @param version
	 * @param elementsSet
	 * @param bond
	 * @param references
	 */
	public Molecule(String id,String version, Set<Const> constSet, Set elementsSet, BondExpr bond, Set<String> references){
		super(id, version, constSet, elementsSet);
		//+ Adding BondProperties
		Set<Property> tmp_properties =  new HashSet<Property>();
		tmp_properties.add(new Property(getId() + ".x" , bond.getX()));
		tmp_properties.add(new Property(getId() + ".y" , bond.getY()));
		tmp_properties.add(new Property(getId() + ".h" , bond.getH()));
		tmp_properties.add(new Property(getId() + ".w" , bond.getW()));
		if(bond.hasSvg()){
			tmp_properties.add(new Property(getId() + ".svg" , bond.getSvg()));
		}
		_properties = Collections.unmodifiableSet(tmp_properties);
		
		//-
		_references = Collections.unmodifiableSet(references);
	}

	/**
	 *
	 * @return
	 */
	public Set<String> getReferences(){
		return _references;
	}

	public Set<Property> evalPropertySet(){
		Set<Property> p = new HashSet<Property>();
		evalPropertySetHelper(p);
		return p;
	}
	private void evalPropertySetHelper(Set<Property> properties){
		if(_properties.isEmpty()) return;
		//+ to avoid infinite adding
		if(properties.contains(_properties.iterator().next()))
			return;
		//-
		//+ adding this.properties
		for(Property p : _properties){
			properties.add(p);
		}
		//-
		//+ add dependencies
		for(String ref : getReferences()){
			Const c = getConst(ref);
			if(c!=null){
				properties.add(c.getProperty());
				continue;
			}
			//
			Molecule m = getMolecule(ref);
			if(m==null) continue; // if molecule don't exist, do nothing.
			m.evalPropertySetHelper(properties);
		}
		//-
		//+ Add all childrenelements and consts to list
		for(Molecule m : this.getElements()){
			m.evalPropertySetHelper(properties);
		}
		for(Const c : this.getConstSet()){
				properties.add(c.getProperty());
		}
		//-
	}
	

}
