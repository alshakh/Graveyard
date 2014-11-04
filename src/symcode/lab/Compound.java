package symcode.lab;

import java.util.Collections;
import java.util.Set;
import symcode.lab.Property.BackupProperty;
import symcode.lab.Property.ConstProperty;
import symcode.lab.Property.EvaluableProperty;
import symcode.lab.Property.NormalProperty;

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
	public Compound(String id, String version, Set<NormalProperty> propertySet, Set<ConstProperty> constsProperties, Set<BondedAtom> atomsSet, Set<String> deps, Set<BackupProperty> backupProperty) {
		super(id, version, propertySet,constsProperties, deps, backupProperty);
	//
	_subAtoms = Collections.unmodifiableSet(atomsSet);
    }

	public Compound(String id, String version, Set<NormalProperty> propertySet, Set<ConstProperty> constsProperties, Set<BondedAtom> _subAtoms, Set<String> deps) {
		this(id, version, propertySet, constsProperties,_subAtoms, deps, Collections.<BackupProperty>emptySet());
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
	protected void addClassSpecificPropertySet(Set<EvaluableProperty> propertySet) {
		for(BondedAtom ba: _subAtoms){
			ba.evaluablePropertySet_Helper(propertySet);
		}
	}
}
