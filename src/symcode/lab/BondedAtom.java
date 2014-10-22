/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package symcode.lab;

import java.util.Set;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public class BondedAtom extends Atom{

	public BondedAtom(String id, String version, Set<Property> propertySet, Set<Property> constsProperties, Set<String> deps) {
		super(id, version, propertySet, constsProperties, deps);
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
