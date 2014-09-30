/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package symcode.lab;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import symcode.expr.Expression;
import symcode.expr.Property;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public abstract class Template implements Loadable {
	//
	
	/**
	 *
	 */
	public static final Set<Molecule> EMPTY_ELEMENTS;
	
	/**
	 *
	 */
	public static final Set<Const> EMPTY_CONSTSET;
	static {
		EMPTY_ELEMENTS = new HashSet<Molecule>();
		EMPTY_CONSTSET = new HashSet<Const>();
	}
	private final String _id;
	private final String _version;
	private final Set<Const> _constSet;
	// map from id to molecule
	private final Set<Molecule> _elements;
	private Template _parent = null;
	
	/**
	 *
	 * @param id
	 * @param version
	 * @param constMap
	 * @param elementsSet
	 */
	public Template(String id, String version, Set<Const> constSet, Set<Molecule> elementsSet) {
		//+
		this._constSet = Collections.unmodifiableSet(constSet);
		//-
		//+ Set parent for elements before adding
		for(Molecule e : elementsSet){
			e.setParent(this);
		}
		this._elements = Collections.unmodifiableSet(elementsSet);
		//-
		//+ Setting ID and Version
		/*
		If no id specified, a random one is assigned. ids need to be 
		distinct in an environment because of environment execution and 
		avoiding adding properties twice.
		*/
		this._id = (id.isEmpty()?Util.generateRandomId():id);
		this._version = version;
		//-
	}
	
	
	/**
	 *
	 * @param constName
	 * @return
	 */
	public Const getConst(String constName) {
		// check _constSet
		for(Const c : _constSet){
			if(c.getName().equals(constName)){
				return c;
			}
		}
		// check in parent
		if (_parent!=null) {
			return _parent.getConst(constName);
		} else {
			return null;
		}
	}

	public Set<Const> getConstSet(){
		return _constSet;
	}
	
	
	/**
	 *
	 * @param elementId
	 * @return
	 */
	public Molecule getMolecule(String elementId){
		for(Molecule m : _elements){
			if (m.getId().equals(elementId))
				return m;
		}
		if(_parent == null) return null;
		return _parent.getMolecule(elementId);
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
	public String getId() {
		return _id;
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
		//
		String elementsRetStr = "";
		boolean firstA = true;
		for (Loadable m : _elements) {
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

	//
	//
	//
	// TODO : (#PARENT-SAVE) better way to save parent and protect it 
	//// to be used only by constructor of molecule
	private boolean parentSetted = false;

	/**
	 *
	 * @param parent
	 */
	protected void setParent(Template parent){
		if(parentSetted) return;
		_parent = parent;
		parentSetted = true;
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
