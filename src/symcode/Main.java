package symcode;

import java.util.ArrayList;
import symcode.evaluator.EvalNode;
import symcode.evaluator.EvaluationError;
import symcode.lab.*;
import symcode.value.Svg;

/**
 *
 * @author Ahmed Alshakh <ahmed.s.alshakh@gmail.com>
 */
public class Main {

	/**
	 * @param args the command line arguments
	 * @throws symcode.evaluator.EvaluationError
	 */
	public static void main(String[] args) throws EvaluationError {
		//System.out.println(new Expr("df.svg").getNeededProperties());
		//
		Lab testLab = symcode.lab.LabLoader.loadLab(new java.io.File("labs/testLab.json"));
		Molecule molecule = testLab.getMolecule("compound_simple");
		//System.out.println(molecule.getEvaluablePropertySet());
		//
		//ArrayList<EvalNode> input = new ArrayList<EvalNode>();
		//input.add(new EvalNode(testLab.getMolecule("atom_outerConst")));
		//input.add(new EvalNode(testLab.getMolecule("atom_innerConst")));
		//EvalNode evlNode = new EvalNode(molecule,input);
		EvalNode evlNode = new EvalNode(molecule);
		
		System.out.println(new Svg(evlNode.eval()).toFullString());
	}
}