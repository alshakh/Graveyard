/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package symcode.lab;

import java.util.Set;

/**
 * this class is the product of all the classes. products cannot be nested, only
 * combined.
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public class Atom extends Molecule {

	/**
	 *
	 * @param id
	 * @param version
	 */
	public Atom(String id, String version, Set<Property> propertySet , Set<Property> constsProperties, Set<String> deps) {
		super(id, version, propertySet, constsProperties, Template.EMPTY_ELEMENTS, deps);
	}

	@Override
	public String toString() {
		return "atom " + super.toString();
	}
}
