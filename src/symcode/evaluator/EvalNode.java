/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package symcode.evaluator;

import symcode.svg.Svg;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptException;
import symcode.lab.BondedAtom;
import symcode.lab.Compound;
import symcode.lab.Molecule;
import symcode.lab.Property;
import symcode.lab.Property.ProductProperty;
import symcode.svg.StripSvg;
import symcode.value.*;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public class EvalNode {

	
	private final Molecule _sym;// either atom or compound 
	private final List<EvalNode> _children; // list because the order of input matters
	private final List<Value> _values; 
	/**
	 *
	 * @param sym
	 * @param inputList
	 */
	public EvalNode(Molecule sym, List<Value> values, List<EvalNode> inputList) {
		_sym = sym;
		_children = inputList;
		_values = values;
	}
	/**
	 *
	 * @param sym
	 */
	public EvalNode(Molecule sym) {
		this(sym, null,null);
	}

	/**
	 *
	 * @return
	 */
	public Product eval() throws EvaluationError {
		//
		EnvironmentBuilder envBuilder = new EnvironmentBuilder();
		envBuilder.addPropertyCollection(_sym.getEvaluablePropertySet());
		//
		if (_children != null) {
			for (int i = 0; i < _children.size(); i++) {
				envBuilder.addPropertyCollection(_children.get(i).eval().getEvaluablePropertySet("$" + (i + 1)));
			}
		}
		if(_values != null) {
			for (int i = 0; i < _values.size(); i++) {
				envBuilder.addProperty(new Property.ConstProperty("$$" + (i + 1), _values.get(i)));
			}
		}
		// fix missing properties with backupProperties and sort()
		envBuilder.prepare(_sym._backupProperties);
		//+ checking validity before executing
		if (envBuilder.isCirculeDependency()) {
			throw new EvaluationError("CircularDependancy: The problem is most likely in the Lab");
		}
		if (envBuilder.isMissingDependecy()) {
			throw new EvaluationError("Some of references are missing: probably the error is less input arguments for " + _sym._id);
		}
		//-
		//+ make environemnt
		Environment evalEnv;
		try {
			evalEnv = envBuilder.toEnvironment();
		} catch (ScriptException ex) {
			Logger.getLogger(EvalNode.class.getName()).log(Level.SEVERE, null, ex);
			throw new EvaluationError("Problem with preparing the environment to evaluate " + _sym._id);
		}
		//-
		// if Atom
		if (_sym.isSingleAtom()) {
			try {
				return processAtom(evalEnv, _sym._id);
			} catch (ScriptException ex) {
				Logger.getLogger(EvalNode.class.getName()).log(Level.SEVERE, null, ex);
				throw new EvaluationError("Problem with evaluating " + _sym._id);
			}
		}//
		// if Compound
		else {
			Set<Product> atomProductSet = new HashSet<Product>();
			//
			for (BondedAtom m : ((Compound) _sym)._subAtoms) {
				try {
					atomProductSet.add(processAtom(evalEnv, m._id));
				} catch (ScriptException ex) {
					Logger.getLogger(EvalNode.class.getName()).log(Level.SEVERE, null, ex);
					throw new EvaluationError("Problem with evaluating bonded atom " + m._id + " for " + _sym._id);
				}
				//	
			}
			try {
				return processCompound(evalEnv, _sym._id, atomProductSet);
			} catch (ScriptException ex) {
				Logger.getLogger(EvalNode.class.getName()).log(Level.SEVERE, null, ex);
				throw new EvaluationError("Problem with evaluating " + _sym._id);
			}
		}
	}

	private Product processAtom(Environment env, String id) throws ScriptException {
		Set<ProductProperty> properties = env.evalMolecule(id);
		return new Product(id, properties);
	}

	private Product processCompound(Environment env, String id, Set<Product> atomProductSet) throws ScriptException {

		Set<ProductProperty> properties = env.evalMolecule(id);
		//
		Svg combinedSvg = null;
		for (Product p : atomProductSet) {
			if (combinedSvg == null) {
				combinedSvg = new StripSvg(p);
			} else {
				combinedSvg = combinedSvg.combine(new StripSvg(p));
			}
		}
		// Svg Property
		properties.add(new ProductProperty("svg", combinedSvg.toStr()));
		//
		return new Product(id, properties);
	}

	@Override
	public String toString() {
		StringBuilder output = new StringBuilder();
		print("", true, output);
		return output.toString();
	}

	private void print(String prefix, boolean isTail, StringBuilder outputBuilder) {
		outputBuilder.append(prefix).append(isTail ? "└── " : "├── ").append(_sym._id).append("\n");
		if (_children == null) {
			return;
		}
		for (int i = 0; i < _children.size() - 1; i++) {
			_children.get(i).print(prefix + (isTail ? "    " : "│   "), false, outputBuilder);
		}
		if (_children.size() > 0) {
			_children.get(_children.size() - 1).print(prefix + (isTail ? "    " : "│   "), true, outputBuilder);
		}
	}
}
