/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package symcode.lab;

import java.util.Set;
import symcode.lab.Property.ConstProperty;
import symcode.lab.Property.NormalProperty;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public class SingleAtom extends Atom{

	public SingleAtom(String id, String version, Set<NormalProperty> propertySet, Set<ConstProperty> constsProperties, Set<String> deps) {
		super(id, version, propertySet, constsProperties, deps);
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
