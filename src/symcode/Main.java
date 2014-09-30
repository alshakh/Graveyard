package symcode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import symcode.expr.EnvironmentPropertyList;
import symcode.lab.*;

/**
 *
 * @author Ahmed Alshakh <ahmed.s.alshakh@gmail.com>
 */
public class Main {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		//
		EnvironmentPropertyList epl = new EnvironmentPropertyList();
		Lab demoLab = symcode.lab.LabLoader.loadLab(new java.io.File("labs/demoLab.json"));
		Molecule a = demoLab.getMolecule("root");
		a.addToPropertyList(epl);
		System.out.println(epl.toEnvironment().toString());



		
		/*
		//
		//
		//
		Lab demoLab = symcode.lab.LabLoader.loadLab(new java.io.File("labs/demoLab.json"));
		Atom a = (Atom)demoLab.getMolecule("simpleAtom");
		
		//EvalNode en = new EvalNode(a);
		//Evaluator evaluator = new Evaluator(demoLab);
		//
		//
		//
		ArrayList exprList = getRandomList();
		printArray(exprList);
		System.out.println("\n\n\n");
		printArray(sortExprs(exprList));*/
	}

	/**
	 *
	 * @return
	 */
	public static ArrayList<ExprPair> getRandomList() {
		ArrayList<ExprPair> exprList = new ArrayList<ExprPair>();
		exprList.add(new ExprPair("aX", "20"));

		exprList.add(new ExprPair("$1.x", "0"));
		exprList.add(new ExprPair("$1.y", "0"));
		exprList.add(new ExprPair("$1.h", "10"));
		exprList.add(new ExprPair("$1.w", "10"));

		exprList.add(new ExprPair("parent.x", "0"));
		exprList.add(new ExprPair("parent.y", "0"));
		exprList.add(new ExprPair("parent.w", "radix.x+radix.w"));
		exprList.add(new ExprPair("parent.h", "radical.y+radical.h"));

		exprList.add(new ExprPair("power.x", "0"));
		exprList.add(new ExprPair("power.y", "0"));
		exprList.add(new ExprPair("power.w", "$1.w"));
		exprList.add(new ExprPair("power.h", "$1.h"));
		exprList.add(new ExprPair("power.svg", "$1.svg"));

		exprList.add(new ExprPair("radical.x", "power.x+power.w-aX"));
		exprList.add(new ExprPair("radical.y", "power.y+power.h-aY"));
		exprList.add(new ExprPair("radical.w", "35"));
		exprList.add(new ExprPair("radical.h", "radix.h+base.h+5"));
		exprList.add(new ExprPair("radical.svg", "RADICAL RADICAL RADICAL"));

		exprList.add(new ExprPair("radix.x", "radical.x+radical.w"));
		exprList.add(new ExprPair("radix.y", "radical.y"));
		exprList.add(new ExprPair("radix.w", "base.w + 5"));
		exprList.add(new ExprPair("radix.h", "23"));
		exprList.add(new ExprPair("radix.svg", "RADIX RADIX RADIX"));

		exprList.add(new ExprPair("base.x", "radical.x+radical.w"));
		exprList.add(new ExprPair("base.y", "radix.y+radix.h"));
		exprList.add(new ExprPair("base.w", "$2.w"));
		exprList.add(new ExprPair("base.h", "$2.h"));
		exprList.add(new ExprPair("base.svg", "$2.svg"));

		exprList.add(new ExprPair("$2.x", "0"));
		exprList.add(new ExprPair("$2.y", "0"));
		exprList.add(new ExprPair("$2.h", "10"));
		exprList.add(new ExprPair("$2.w", "10"));

		exprList.add(new ExprPair("aY", "30"));

		return exprList;
	}

	/**
	 *
	 * @param exprList
	 * @return
	 */
	public static HashSet<String> getAllRefs(ArrayList<ExprPair> exprList) {
		HashSet<String> allRefs = new HashSet<String>();
		for (ExprPair a : exprList) {
			allRefs.add(a.key);
		}
		return allRefs;
	}

	/**
	 *
	 * @param exprList
	 * @param key
	 * @return
	 */
	public static int indexOfExprPair(ArrayList<ExprPair> exprList, String key) {
		for (int i = 0; i < exprList.size(); i++) {
			if (exprList.get(i).key.equals(key)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 *
	 * @param exprList
	 * @return
	 */
	public static ArrayList<ExprPair> sortExprs(ArrayList<ExprPair> exprList) {
		exprList = (ArrayList<ExprPair>) exprList.clone();
		HashSet<String> allRefs = getAllRefs(exprList);
		//// assuming no circular dependencies
		for (int i = 0; i < exprList.size(); i++) {
			{
				ExprPair exprPair = exprList.get(i);
				HashSet<String> dependsOnSet = exprPair.extractReferences(allRefs);

				HashSet<String> depsCache = new HashSet<String>();
				while (!dependsOnSet.isEmpty()) {
					int depIdx = -1;
					boolean noDepFixing = true;
					for (String dep : dependsOnSet) {
						depIdx = indexOfExprPair(exprList, dep);
						// depidx =-1 if non existant
						if (depIdx > i) {
							noDepFixing = false;
							break;
						}
					}
					if (noDepFixing) {
						break;
					} else {
						if (depsCache.contains(exprList.get(i).key)) {
							System.out.println("Circular dependency");
							System.exit(1);
						}
						depsCache.add(exprList.get(i).key);
						Collections.swap(exprList, i, depIdx);
						dependsOnSet = exprList.get(i).extractReferences(allRefs);
					}
				}
			}
		}
		////
		return exprList;
	}

	/**
	 *
	 * @param exprList
	 */
	public static void printArray(ArrayList<ExprPair> exprList) {
		for (ExprPair ap : exprList) {
			System.out.println(ap.toString());
		}
	}
}

class ExprPair {

	public final String key;
	public final String value;

	public ExprPair(String k, String v) {
		key = k;
		value = v;
	}

	public String toString() {
		return key + " = " + value;
	}

	public HashSet<String> extractReferences(HashSet<String> allRefs) {
		HashSet<String> deps = new HashSet<String>();
		for (String ref : allRefs) {
			if (value.contains(ref)) {
				deps.add(ref);
			}
		}
		return deps;
	}
}
/*
 aX = 20
 aY = 30

 $1.x = 0
 $1.y = 0
 $1.h = 10
 $1.w = 10

 parent.x = 0
 parent.y = 0
 parent.w = radix.x+radix.w
 parent.h = radical.y+radical.h
 
 power.x = 0
 power.y = 0
 power.w = $1.w
 power.h = $1.h
 power.svg = $1.svg


 radical.x = power.x+power.w-aX
 radical.y = power.y+power.h-aY
 radical.w = 35
 radical.h = radix.h+base.h+5
 radical.svg = RADICAL RADICAL RADICAL

 radix.x = radical.x+radical.w
 radix.y = radical.y
 radix.w = base.w + 5
 radix.h = 23
 radix.svg=RADIX RADIX RADIX


 base.x = radical.x+radical.w
 base.y = radix.y+radix.h
 base.w = $2.w
 base.h = $2.h
 base.svg = $2.svg

 $2.x = 0
 $2.y = 0
 $2.h = 10
 $2.w = 10
 */
