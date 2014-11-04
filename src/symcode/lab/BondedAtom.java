/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package symcode.lab;

import java.util.Collections;
import java.util.Set;
import symcode.lab.Property.BackupProperty;
import symcode.lab.Property.ConstProperty;
import symcode.lab.Property.NormalProperty;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public class BondedAtom extends Atom{

	public BondedAtom(String id, String version, Set<NormalProperty> propertySet, Set<ConstProperty> constsProperties, Set<String> deps, Set<BackupProperty> backupProperties) {
		super(id, version, propertySet, constsProperties, deps, backupProperties);
	}

	public BondedAtom(String id, String version, Set<NormalProperty> propertySet, Set<ConstProperty> constsProperties, Set<String> deps) {
		this(id, version, propertySet, constsProperties, deps, Collections.<BackupProperty>emptySet());
	}

	/**
	 * return a Molecule instance with id. It will only search in the containing compound.
	 * @param id
	 * @return 
	 */
	@Override
	public Molecule getMolecule(String id) {
		return this.getParent().getMolecule(id);
	}
}
