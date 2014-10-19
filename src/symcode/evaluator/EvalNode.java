/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package symcode.evaluator;

import java.util.ArrayList;
import java.util.HashSet;
import symcode.expr.Environment;
import symcode.expr.EnvironmentPropertyList;
import symcode.lab.Molecule;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public class EvalNode {
	private final Molecule _sym;// either atom or compound 
	private final ArrayList<EvalNode> _inputNodes;

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
		//+ constructing environment
		EnvironmentPropertyList epl = new EnvironmentPropertyList();
		epl.addPropertySet(_sym.evalPropertySet());
		//
		if(_inputNodes != null){
			for(int i = 0 ; i < _inputNodes.size() ; i++){
				_inputNodes.get(i).eval().addEnvironmentPropertyList("$"+i, epl);
			}
		}
		//+ checking validity before executing
		if(!epl.isValid()){
			if(epl.isCircularDepedency()){
				throw new EvaluationError("CircularDependancy: The problem is most likely in the Lab");
			} else {
				throw new EvaluationError("Some of references are missing: probably the error is less input arguments for "+_sym.getId());
			}
		}
		//-
		Environment env = epl.toEnvironment();
		//- environment is ready
		
		// if Atom
		double x = Double.valueOf(env.resolveRef(_sym.getId()+".x"));
		double y = Double.valueOf(env.resolveRef(_sym.getId()+".y"));
		double w = Double.valueOf(env.resolveRef(_sym.getId()+".w"));
		double h = Double.valueOf(env.resolveRef(_sym.getId()+".h"));
		String svg = env.resolveRef(_sym.getId()+".svg");
		return new Product(new BondValue(svg,Double.valueOf(x),y,h,w));
	}
}
