
package symcode.lab;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * This class will handle the lab information and provide it to the processing class.
 * The data structure should look like this :-
 * Lab
 *  ├──SingleAtom
 *  └──Compound
 *         └──BondedAtom
 * @author Ahmed Alshakh <ahmed.s.alshakh@gmail.com>
 */
public class Lab extends Template {

	public static final Set<Molecule> EMPTY_ELEMENTS = Collections.emptySet();
	//
	public final Set<Molecule> _elements;
	/**
	 *
	 * @param id
	 * @param version
	 * @param propertySet
	 * @param constsProperties
	 * @param elementsSet
	 */
	public Lab(String id,String version, Set<Property> constsProperties, Set<Molecule> elementsSet)
	{
             super( id, version, constsProperties);
		//+ Set parent for elements before adding
		for(Molecule m : elementsSet){
			m.initParent(this);
		}
		_elements = Collections.unmodifiableSet(elementsSet);
		//-
	}
	@Override
	public String toString(){
		return "lab "+super.toString();
	}

	@Override
	public Molecule getMolecule(String id) {
		for(Molecule e: _elements){
			if(e._id.equals(id)) return e;
		}
		return null;
	}
}