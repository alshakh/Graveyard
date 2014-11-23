/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package symcode.evaluator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import symcode.evaluator.Parser.MoleculeParseNode;
import symcode.evaluator.Parser.ParseNode;
import symcode.lab.Lab;
import symcode.lab.Molecule;
import symcode.lab.nativemolecule.EmptyAtom;
import symcode.lab.nativemolecule.NumberCompound;
import symcode.lab.nativemolecule.TextCompound;
import symcode.lab.nativemolecule.WrapperCompound;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public class Evaluator {

	/**
	 *
	 */
	public final Set<Lab> _labs;

	/**
	 *
	 * @param labs
	 */
	public Evaluator(HashSet<Lab> labs) {
		_labs = labs;
	}

	/**
	 *
	 * @param lab
	 */
	public Evaluator(Lab lab) {
		this(new HashSet<Lab>(java.util.Arrays.asList(new Lab[]{lab})));
	}

	/**
	 *
	 * @param inputCode
	 * @return
	 */
	public Product eval(String inputCode) throws SyntaxError, EvaluationError {
		EvalNode evalTree = constructEvalTree(inputCode);
		return evalTree.eval();
	}

	private EvalNode constructEvalTree(String code) throws SyntaxError {
		return constructEvalTree_helper(new Parser(code)._parseTree);
	}

	private Molecule getMolecule(String moleculeName) {
		// TODO : check if has children or not. to give priority to compound or atom
		for (Lab l : _labs) {
			Molecule m = l.getMolecule(moleculeName);
			if (m != null) {
				return m;
			}
		}
		return null;
	}

	private EvalNode constructEvalTree_helper(ParseNode parseNode) throws SyntaxError {
		List<EvalNode> children = null;
		if (parseNode.isMolecule()
		    && ((Parser.MoleculeParseNode) parseNode)._children != null) {
			children = new ArrayList<EvalNode>();
			for (ParseNode childParseNode : ((Parser.MoleculeParseNode) parseNode)._children) {
				children.add(constructEvalTree_helper(childParseNode));
			}
		}
		switch (parseNode._type) {
			case EMPTY:
				return new EvalNode(this, EmptyAtom.INSTANCE);
			case WRAPPER:
				if (children != null) {
					return new EvalNode(this, 
						new WrapperCompound(children.size()), null, children);
				} else {
					return new EvalNode(this, EmptyAtom.INSTANCE);
				}
			case MOLECULE:
				MoleculeParseNode mpn = (MoleculeParseNode)parseNode;
				Molecule m = getMolecule(mpn._me);
				if (m == null) {
					throw new SyntaxError("cannot find molecule " + mpn._me);
				}
				return new EvalNode(this, getMolecule(mpn._me),mpn._values, children);
			case NUMBER:
				return new EvalNode(this, new NumberCompound(parseNode._me));
			case QUOTED:
				return new EvalNode(this, new TextCompound(parseNode._me));
			default:
				throw new RuntimeException("SOMETHING IS WRONG WITH THE EVALUATOR -- constructEvalTree_helper() : " + parseNode._me);
		}
	}
}
