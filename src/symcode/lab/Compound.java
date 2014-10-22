package symcode.lab;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * All classes in lab should fallow this abstract class.
 *
 * @author Ahmed Alshakh <ahmed.s.alshakh@gmail.com>
 */
public class Compound extends Molecule {
	public static final Set<BondedAtom> EMPTY_ATOMS_SET = Collections.emptySet();
	public final Set<BondedAtom> _subAtoms;

	/**
	 *
	 * @param id
	 * @param version
	 * @param propertySet
	 * @param deps
	 */
	
	/**
	 *
	 * @param id
	 * @param version
	 * @param propertySet
	 * @param constsProperties
	 * @param atomsSet
	 * @param deps
	 */
	public Compound(String id, String version, Set<Property> propertySet, Set<Property> constsProperties, Set<BondedAtom> atomsSet, Set<String> deps) {
		super(id, version, propertySet,constsProperties, deps);
	//
	_subAtoms = Collections.unmodifiableSet(atomsSet);
    }

    public String toString(){
	    return "compound "+super.toString();
    }

	@Override
	public Molecule getMolecule(String id) {
		for(BondedAtom a: _subAtoms){
			if(a._id.equals(id)){
				return (Molecule)a;
			}
		}
		return null;
	}

	@Override
	void addClassSpecificPropertySet(Set<Property> propertySet) {
		for(BondedAtom ba: _subAtoms){
			ba.evaluablePropertySet_Helper(propertySet);
		}
	}
}
