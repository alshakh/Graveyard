/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package symcode.evaluator;

import java.util.ArrayList;
import java.util.HashSet;
import symcode.lab.Molecule;
import symcode.value.*;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public class EvalNode {
	private final Molecule _sym;// either atom or compound 
	private final ArrayList<EvalNode> _inputNodes; // list because the order of input matters

	/**
	 *
	 * @param sym
	 * @param inputList
	 */
	public EvalNode(Molecule sym, ArrayList<EvalNode> inputList){
		_sym = sym;
		_inputNodes = inputList;
	}
	
	/**
	 *
	 * @param sym
	 */
	public EvalNode(Molecule sym){
		this(sym,null);
	}

	/**
	 *
	 * @return
	 */
	public Product eval() throws EvaluationError{
		//
		Environment evaluationEvironment = new Environment();
		evaluationEvironment.addPropertyCollection(_sym.getEvaluablePropertySet());
		//
		if(_inputNodes != null){
			for(int i = 0 ; i < _inputNodes.size() ; i++){
				_inputNodes.get(i).eval().getEvaluablePropertySet("$"+i);
			}
		}
		//+ checking validity before executing
		if(evaluationEvironment.inspect() == Environment.CIRCULAR_DEPENDENCY)
				throw new EvaluationError("CircularDependancy: The problem is most likely in the Lab");
		if(evaluationEvironment.inspect() == Environment.MISSING_DEPENDENCY)
				throw new EvaluationError("Some of references are missing: probably the error is less input arguments for "+_sym._id);
		//-
		
		// if Atom
		if(_sym.isAtom()){
			Doub x = (Doub)evaluationEvironment.resolveReference(_sym._id+".x");
			Doub y = (Doub)evaluationEvironment.resolveReference(_sym._id+".y");
			Doub h = (Doub)evaluationEvironment.resolveReference(_sym._id+".h");
			Doub w = (Doub)evaluationEvironment.resolveReference(_sym._id+".w");
			Svg svg = new Svg(evaluationEvironment.resolveReference(_sym._id+".svg").toString());
			return new Product(svg,x,y,h,w);
		} else { // Compound
			return null;
		}
	}
}
