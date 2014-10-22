/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package symcode.evaluator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import symcode.lab.Molecule;
import symcode.value.*;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public class EvalNode {
	private final Molecule _sym;// either atom or compound 
	private final List<EvalNode> _inputNodes; // list because the order of input matters

	/**
	 *
	 * @param sym
	 * @param inputList
	 */
	public EvalNode(Molecule sym, List<EvalNode> inputList){
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
				evaluationEvironment.addPropertyCollection(_inputNodes.get(i).eval().getEvaluablePropertySet("$"+(i+1)));
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
			return processAtom(evaluationEvironment, _sym._id);
		} else { // Compound
			Set<Product> atomProductSet = new HashSet<Product>();
			for(Molecule m : _sym._elements){
				atomProductSet.add(processAtom(evaluationEvironment, m._id));
			}
			return processCompound(evaluationEvironment, _sym._id, atomProductSet);
		}
	}
	private Product processAtom(Environment env, String id){
			Doub x = (Doub)env.resolveReference(id+".x");
			Doub y = (Doub)env.resolveReference(id+".y");
			Doub h = (Doub)env.resolveReference(id+".h");
			Doub w = (Doub)env.resolveReference(id+".w");
			Str svg = new Str(env.resolveReference(id+".svg").toString());
			return new Product(id, svg,x,y,h,w);
	}
	private Product processCompound(Environment env, String id, Set<Product> atomProductSet){
			Doub x = (Doub)env.resolveReference(id+".x");
			Doub y = (Doub)env.resolveReference(id+".y");
			Doub h = (Doub)env.resolveReference(id+".h");
			Doub w = (Doub)env.resolveReference(id+".w");
			//
			// TODO : #SVG evaluating actual svg must be separated
			StringBuilder svgBodySB = new StringBuilder();
			for(Product p : atomProductSet){
				svgBodySB.append(p.toSvgString()).append("\n");
			}
			Str svg = new Str(svgBodySB.substring(0, svgBodySB.length()-1));
			//
			return new Product(id, svg,x,y,h,w);
	}
}
