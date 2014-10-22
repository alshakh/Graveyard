/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package symcode.lab;

import java.util.Collections;
import java.util.Set;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public abstract class Template{
	public static final Set<Property> EMPTY_CONSTS = Collections.emptySet();
	//
	public final String _id;
	public final String _version;
	public final Set<Property> _constProperties;
	//
	private Template _parent = null; // Only non-final member only changed from the constructor of this class.
	
	/**
	 *
	 * @param id
	 * @param version
	 * @param elementsSet
	 */
	public Template(String id, String version, Set<Property> constsProperties) {
		//+ Set constsProperties
		this._constProperties = Collections.unmodifiableSet(constsProperties);
		//-
		//+ Setting ID and Version
		this._id = id;
		this._version = version;
		//-
	}

	public Template getParent() {
		return _parent;
	}
	
	public final Property getConst(String id) {
		for(Property p: _constProperties){
			if(p._id.equals(id)) {
				return p;
			}
		}
		if(getParent()!=null)
			return getParent().getConst(id);
		return null;
	}

	public abstract Molecule getMolecule(String id);

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
	

	@Override
	public String toString() {
		return _id;
		/*
		String SPACE = "    ";
		String myRetStr = "";
		myRetStr += "id: " + this._id;
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
			*/
	}

	private boolean parentInitiated = false;
	void initParent(Template parent) {
		if(parentInitiated) throw new UnsupportedOperationException("You can't initiated parent for ["+_id +"] twice");
		_parent = parent;
		parentInitiated = true;
	}
}
