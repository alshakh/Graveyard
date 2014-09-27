/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package symcode.lab;

import java.util.HashMap;
import java.util.HashSet;
import symcode.expr.Expression;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public abstract class Template implements Loadable {
	//
	
	/**
	 *
	 */
	public static final HashSet<Molecule> EMPTY_ELEMENTS;
	
	/**
	 *
	 */
	public static final HashMap<String, Expression> EMPTY_CONST;
	//
	static {
		EMPTY_ELEMENTS = new HashSet<Molecule>();
		EMPTY_CONST = new HashMap<String, Expression>();
	}
	//
	private final String _id;
	//
	private final String _version;
	//
	private final HashMap<String, Expression> _const;
	// map from id to molecule
	private final HashSet<Molecule> _elements;
	//
	private Template _parent = null;
	//
	
	/**
	 *
	 * @param id
	 * @param version
	 * @param constMap
	 * @param elementsSet
	 */
	public Template(String id, String version, HashMap<String, Expression> constMap, HashSet<Molecule> elementsSet) {
		this._const = constMap;
		/////////////////
		// Set parent for parents
		for(Molecule e : elementsSet){
			e.setParent(this);
		}
		this._elements = elementsSet;
		/////////////////
		this._id = id;
		this._version = version;
	}
	
	//
	
	/**
	 *
	 * @param constName
	 * @return
	 */
	public int hasConst(String constName) {
		throw new UnsupportedOperationException();
	}
	
	//
	
	/**
	 *
	 * @param constName
	 * @return
	 */
	public double getConst(String constName) {
		throw new UnsupportedOperationException();
	}
	
	//
	
	/**
	 *
	 * @param elementId
	 * @return
	 */
	public int hasMolecule(String elementId){
		throw new UnsupportedOperationException();
	}
	//
	
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
		return null;
	}
	//

	public boolean isAtom(){
		return (this.getClass().equals(Atom.class));
	}
	public boolean isLab(){
		return this.getClass().equals(Lab.class);
	}
	public boolean isCompound(){
		return this.getClass().equals(Compound.class);
	}
	//
	
	/**
	 *
	 * @return
	 */
	public String getId() {
		return _id;
	}
	
	//
	
	/**
	 *
	 * @return
	 */
	public String getVersion() {
		return _version;
	}
	
	//
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
	protected void setParent(Template parent){
		if(parentSetted) return;
		_parent = parent;
		parentSetted = true;
	}
	public Template getParent(){
		return _parent;
	}
}
