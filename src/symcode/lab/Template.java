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
public abstract class Template{
	//
	
	/**
	 *
	 */
	public static final Set<Molecule> EMPTY_ELEMENTS = new HashSet<Molecule>();
	public static final Set<Property> EMPTY_PROPERTY_SET = Collections.unmodifiableSet(new HashSet<Property>());
	//
	public final String _id;
	public final String _version;
	public final Set<Property> _properties; // properties
	public final Set<Property> _constProperties;
	public final Set<Molecule> _elements;
	//
	public Template _parent = null; // Only non-final member only changed from the constructor of this class.
	
	/**
	 *
	 * @param id
	 * @param version
	 * @param elementsSet
	 */
	public Template(String id, String version, Set<Property> propertySet, Set<Property> constsProperties, Set<Molecule> elementsSet) {
		//+ Set parent for elements before adding
		for(Template e : elementsSet){
			e._parent = this;
		}
		this._elements = Collections.unmodifiableSet(elementsSet);
		//-
		//+ Set constsProperties
		this._constProperties = Collections.unmodifiableSet(constsProperties);
		//-
		//+ Setting ID and Version
		this._id = id;
		this._version = version;
		//-
		//+ properties
		this._properties =Collections.unmodifiableSet(propertySet); 
		//-
	}
	
	

	/**
	 *
	 * @return
	 */
	public Set<Property> getPropertySetOf(String objectRef) {
		Set<Property> ps = null;
		// objectRef is a js objectRef.
		for(Property p : _properties){
			if(p.getObjectReference().equals(objectRef)){
				//+ create the HashSet instance only if actually used,
				// because if no refernce found it will be useless
				if(ps == null ) {
					ps= new HashSet<Property>();
				}
				//-
				ps.add(p);
			}
		}
		// check if any objectRef exists, if so return the set
		//	it will always be null if no objectRef found because 
		//	the instance is created in time
		if(ps!=null) return ps;
		// if no objectRef found check parent 
		if (_parent!=null) {
			return _parent.getPropertySetOf(objectRef);
		} else {
			return null;
		}
	}
	
	
	/**
	 *
	 * @param elementId
	 * @return
	 */
	public Molecule getMolecule(String elementId){
		for(Molecule m : _elements){
			if (m._id.equals(elementId))
				return m;
		}
		if(_parent == null) return null;
		return _parent.getMolecule(elementId);
	}

	public Property getConst(String constRef){
		for(Property p: _constProperties){
			if(p._id.equals(constRef)) return p;
		}
		if (_parent != null) {
			return _parent.getConst(constRef);
		}
		return null;
	}

	/**
	 *
	 * @return
	 */
	public boolean isAtom(){
		return (this.getClass().equals(Atom.class));
	}

	/**
	 *
	 * @return
	 */
	public boolean isLab(){
		return this.getClass().equals(Lab.class);
	}

	/**
	 *
	 * @return
	 */
	public boolean isCompound(){
		return this.getClass().equals(Compound.class);
	}
	
	/**
	 *
	 * @return
	 */
	public String getVersion() {
		return _version;
	}
	
	@Override
	public String toString() {
		String SPACE = "    ";
		String myRetStr = "";
		myRetStr += "id: " + this._id;
		for(Property p : _properties){
			myRetStr += "\n"+p.toString();
		}
		//
		String elementsRetStr = "";
		boolean firstA = true;
		for (Template m : _elements) {
			if (m == null) {
				continue;
			}
			if (firstA) {
				firstA = false;
			} else {
				elementsRetStr += "\n";
			}
			//
			String[] lines = m.toString().split("\n");
			boolean firstB = true;
			for (String l : lines) {
				if (firstB) {
					firstB = false;
				} else {
					elementsRetStr += "\n";
				}
				elementsRetStr += SPACE + l;
			}
		}
		return myRetStr + (elementsRetStr.length() != 0 ? "\n" + elementsRetStr : "");
	}

	/**
	 *
	 * @return
	 */
	public Template getParent(){
		return _parent;
	}
	public Set<Molecule> getElements(){
		return _elements;
	}



}
