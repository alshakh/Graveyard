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
		Set<Property> tmpProperties = new HashSet<Property>();
		tmpProperties.add(new Property(getId() + ".x" , bond.getX()));
		tmpProperties.add(new Property(getId() + ".y" , bond.getY()));
		tmpProperties.add(new Property(getId() + ".h" , bond.getH()));
		tmpProperties.add(new Property(getId() + ".w" , bond.getW()));
		 _properties = Collections.unmodifiableSet(tmpProperties);
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

	public void addToPropertyList(EnvironmentPropertyList epl){
		//+ to avoid infinite adding
		if(epl.hasReference(getId())) 
			return;
		epl.addReference(getId());
		//-
		//+ adding this.properties
		for(Property p : _properties){
			epl.addProperty(p);
		}
		//-
		//+ add dependencies
		for(String ref : getReferences()){
			Const c = getConst(ref);
			if(c!=null){
				c.addToEnvironmentPropertyList(epl);
				continue;
			}
			//
			Molecule m = getMolecule(ref);
			if(m==null) continue; // if molecule don't exist, do nothing.
			m.addToPropertyList(epl);
		}
		//-
		//+ Add all childrenelements and consts to list
		for(Molecule m : this.getElements()){
			m.addToPropertyList(epl);
		}
		for(Const c : this.getConstSet()){
			c.addToEnvironmentPropertyList(epl);
		}
		//-
		//+ adding extra stuff (Atom may be different than compound)
		addExtraToEnvironmentPropertyList(epl);
		//-
	}

	public abstract void addExtraToEnvironmentPropertyList(EnvironmentPropertyList epl);
}
