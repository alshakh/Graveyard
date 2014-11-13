/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package symcode.lab;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import symcode.lab.Property;

/**
 * @author Ahmed Alshakh www.alshakh.net
 */
public abstract class Atom extends Molecule {

	/**
	 *
	 * @param id
	 * @param version
	 * @param propertySet
	 * @param constsProperties
	 * @param deps
	 * @param backupProperties
	 */
	public Atom(String id, String version, Set<Property> properties) {
		super(id, version, properties);
	}

	@Override
	public String toString() {
		return "atom " + super.toString();
	}
}
