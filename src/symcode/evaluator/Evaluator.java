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
import symcode.evaluator.Parser.ParseNode;
import symcode.lab.Lab;
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
	public Evaluator(HashSet<Lab> labs){
		_labs = labs;
	}

	/**
	 *
	 * @param lab
	 */
	public Evaluator(Lab lab){
		this(new HashSet<Lab>(java.util.Arrays.asList(new Lab[]{lab})));
	}

	/**
	 *
	 * @param inputCode
	 * @return
	 */
	public Product eval(String inputCode){
		EvalNode evalTree;
		try {
			evalTree = constructEvalTree(inputCode);
			return evalTree.eval();
		} catch (SyntaxError ex) {
			// TODO ; display message and do something
			return null;
		} catch (EvaluationError ex) {
			// TODO ; display message and do something
			return null;
		}
	}

	private EvalNode constructEvalTree(String code) throws SyntaxError{
		return constructEvalTree_helper(new Parser(code)._parseTree);
	}

	private EvalNode constructEvalTree_helper(ParseNode parseNode){
		List<EvalNode> children = null;
		if(! parseNode.isLeaf()){
			children = new ArrayList<EvalNode>();
			for(ParseNode childParseNode : parseNode._children){
				children.add(constructEvalTree_helper(childParseNode));
			}
		}
		switch(parseNode._type){
			case EMPTY:
				return new EvalNode(EmptyAtom.INSTANCE);
			case WRAPPER:
				if(children!=null){
					return new EvalNode(new WrapperCompound(children.size())
					, children);
				} else {
					return new EvalNode(EmptyAtom.INSTANCE);
				}
			case MOLECULE:
				throw new UnsupportedOperationException();

			case NUMBER:
				return new EvalNode(new NumberCompound(parseNode._me));
			case QUOTED:
				return new EvalNode(new TextCompound(parseNode._me));
			default:
				throw new RuntimeException("SOMETHING IS WRONG WITH THE EVALUATOR -- constructEvalTree_helper() : "+parseNode._me);
		}
	}
}
