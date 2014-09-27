/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package symcode.evaluator;

import java.util.ArrayList;
import java.util.HashSet;
import symcode.expr.ExprEnvironment;
import symcode.lab.Molecule;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public class EvalNode {
	private final Molecule _sym;// either atom or compound 
	private final ArrayList<EvalNode> _inputNodes;

	//
	public EvalNode(Molecule sym, ArrayList<EvalNode> inputList){
		_sym = sym;
		_inputNodes = inputList;
	}
	
	//
	public EvalNode(Molecule sym){
		this(sym,new ArrayList<EvalNode>());
	}

	//
	public Product eval(){
		if(_sym.isAtom()){
			// Prepare ExprEnvironment
			ExprEnvironment expEnv;

			//

		}
		//
		throw new UnsupportedOperationException();
	}
}
