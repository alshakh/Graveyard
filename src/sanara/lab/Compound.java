package sanara.lab;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import sanara.lab.Property;

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
	public Compound(String id, String version, Set<Property> properties, Set<BondedAtom> subAtoms) {
		super(id, version, properties);
		_subAtoms = Collections.unmodifiableSet(subAtoms);
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
	public void insertPropertiesToBuilder(EvaluablePropertiesBuilder builder){
		builder.addProperties(_properties);
		for(BondedAtom atom : _subAtoms){
			atom.insertPropertiesToBuilder(builder);
		}
	}
}