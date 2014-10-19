/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package symcode.lab;

import java.util.Set;
import symcode.expr.Expression;

/**
 * this class is the product of all the classes. products cannot be nested, only
 * combined.
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public class Atom extends Molecule implements Loadable {

	/**
	 *
	 */
	public static final Atom DEBUG_ATOM = new Atom(
		"",
		"",
		Template.EMPTY_CONSTSET,
		new BondExpr(
			new Expression("0"),
			new Expression("0"),
			new Expression("10"),
			new Expression("10")
		),
		Molecule.EMPTY_REFERENCES
	);

	/**
	 *
	 * @param id
	 * @param version
	 * @param constSet
	 * @param bond
	 * @param references
	 */
	public Atom(String id, String version, Set<Const> constSet, BondExpr bond, Set<String> references) {
		super(id, version, constSet, Template.EMPTY_ELEMENTS, bond, references);
	}

	@Override
	public String toString() {
		return "atom " + super.toString();
	}
}
