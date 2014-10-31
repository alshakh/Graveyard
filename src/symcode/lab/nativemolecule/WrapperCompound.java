package symcode.lab.nativemolecule;

import java.util.HashSet;
import java.util.Set;

import symcode.lab.BondedAtom;
import symcode.lab.Compound;
import symcode.lab.Molecule;
import symcode.lab.Property.NormalProperty;
import symcode.lab.Template;
import symcode.value.Doub;
import symcode.value.Expr;
import symcode.value.SvgExpr;

public class WrapperCompound extends Compound {
	private static final String ID = "wrapper";
	private static final String subAtomPrefix = "container_";

	public WrapperCompound(int inputSize) {
		super(ID, "", generateProperties(inputSize), Template.EMPTY_CONSTS,
				generateAtomSet(inputSize), Molecule.EMPTY_DEPENDENCIES);
	}

	private static Set<NormalProperty> generateProperties(int n) {
		Set<NormalProperty> ps = new HashSet<NormalProperty>();
		ps.add(new NormalProperty(ID, "x", new Doub("0")));
		ps.add(new NormalProperty(ID, "y", new Doub("0")));
		ps.add(new NormalProperty(ID, "h", new Expr("Math.max("+generatePropertyString(n,"h")+")")));
		ps.add(new NormalProperty(ID, "w", new Expr(generatePropertyString(n, "+","w"))));
		return ps;
	}
	
	private static String generatePropertyString(int n, String postfix){
		return generatePropertyString(n, ", ", postfix);
	}
	private static String generatePropertyString(int n,String infix,String postfix){
		StringBuilder sb = new StringBuilder();
		for(int i=1; i<=n ;i++){
			sb.append("$").append(i).append(".").append(postfix);
			if(i!=n) sb.append(infix);
		}
		return sb.toString();
	}

	private static Set<BondedAtom> generateAtomSet(int n) {
		Set<BondedAtom> atoms = new HashSet<BondedAtom>();
		for(int i = 1; i<= n; i++){
			atoms.add(generateAtomNo(i));
		}
		return atoms;
	}

	private static BondedAtom generateAtomNo(int i) {
		String prevId = subAtomPrefix + (i - 1);
		String id = subAtomPrefix + i;
		Set<NormalProperty> properties = new HashSet<NormalProperty>();
		if (i == 1)
			properties.add(new NormalProperty(id, "x", new Doub("0")));
		else
			properties.add(new NormalProperty(id, "x", new Expr(prevId + ".x+"
					+ prevId + ".w")));
		properties.add(new NormalProperty(id, "y", new Doub("0")));
		properties.add(new NormalProperty(id, "w", new Expr("$" + i + ".w")));
		properties.add(new NormalProperty(id, "h", new Expr("$" + i + ".h")));
		properties.add(new NormalProperty(id, "svg", new SvgExpr("<<< $" + i
				+ ".svg >>>")));
		return new BondedAtom(id, "", properties,
				Template.EMPTY_CONSTS, Molecule.EMPTY_DEPENDENCIES);
	}
}
