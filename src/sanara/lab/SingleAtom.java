/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sanara.lab;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import sanara.lab.Property;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public class SingleAtom extends Atom{

	public SingleAtom(String id, String version, Set<Property> properties) {
		super(id, version, properties);
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
