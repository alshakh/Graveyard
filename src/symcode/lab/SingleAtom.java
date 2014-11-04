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
public class SingleAtom extends Atom{

	public SingleAtom(String id, String version, Set<NormalProperty> propertySet, Set<ConstProperty> constsProperties, Set<String> deps, Set<BackupProperty> backupProperties) {
		super(id, version, propertySet, constsProperties, deps, backupProperties);
	}

	public SingleAtom(String id, String version, Set<NormalProperty> propertySet, Set<ConstProperty> constsProperties, Set<String> deps) {
		this(id, version, propertySet, constsProperties, deps, Collections.<BackupProperty>emptySet());
	}


	/**
	 * the method will return null, because SingleAtom should not have access to other molecules.
	 * @param id
	 * @return Always return null
	 */
	@Override
	public Molecule getMolecule(String id) {
		return null;
	}

}
