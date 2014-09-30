/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package symcode.lab;

import java.util.HashSet;
import java.util.Set;
import symcode.expr.EnvironmentPropertyList;
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
		Molecule.EMPTY_REFERENCES,
		new SvgString("<rect width=\"10px\" height=\"10px\" style=\"fill:rgb(255,0,0);stroke:rgb(0,0,0)\">")
	);
	private final SvgString _svgString;

	/**
	 *
	 * @param id
	 * @param version
	 * @param constMap
	 * @param bond
	 * @param references
	 * @param svgString
	 */
	public Atom(String id, String version, Set<Const> constSet, BondExpr bond, Set<String> references, SvgString svgString) {
		super(id, version, constSet, Template.EMPTY_ELEMENTS, bond, references);
		this._svgString = svgString;
	}

	/**
	 *
	 * @return
	 */
	public SvgString getSvgString() {
		return _svgString;
	}

	@Override
	public String toString() {
		return "atom " + super.toString();
	}

	/**
	 * Adding SvgString to Environment.
	 *
	 * @param epl
	 */
	@Override
	public void addExtraToEnvironmentPropertyList(EnvironmentPropertyList epl) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
