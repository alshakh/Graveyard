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
import symcode.lab.BondedAtom;
import symcode.lab.Compound;
import symcode.lab.Molecule;
import symcode.lab.Property;
import symcode.lab.Property.ProductProperty;
import symcode.lab.SingleAtom;
import symcode.svg.StripSvg;
import symcode.value.*;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public class EvalNode {

	public final Molecule _sym;// either atom or compound 
	public final List<EvalNode> _children; // list because the order of input matters
	public final List<Value> _values;
	public final Evaluator _evaluator;

	/**
	 *
	 * @param evaluator
	 * @param sym
	 * @param values
	 * @param inputList
	 */
	public EvalNode(Evaluator evaluator, Molecule sym, List<Value> values, List<EvalNode> inputList) {
		_sym = sym;
		_children = inputList;
		_values = values;
		_evaluator = evaluator;
	}

	/**
	 *
	 * @param evaluator
	 * @param sym
	 */
	public EvalNode(Evaluator evaluator, Molecule sym) {
		this(evaluator, sym, null, null);
	}

	/**
	 *
	 * @return
	 * @throws symcode.evaluator.EvaluationError
	 * @throws symcode.evaluator.SyntaxError
	 */
	public Product eval() throws EvaluationError, SyntaxError {
		//
		EnvironmentBuilder envBuilder = new EnvironmentBuilder();
		envBuilder.addPropertyCollection(_sym.getEvaluablePropertySet());
		//
		if (_children != null) {
			for (int i = 0; i < _children.size(); i++) {
				envBuilder.addPropertyCollection(_children.get(i).eval().getEvaluablePropertySet("$" + (i + 1)));
			}
		}
		if (_values != null) {
			for (int i = 0; i < _values.size(); i++) {
				envBuilder.addProperty(new Property.ConstProperty("$$" + (i + 1), _values.get(i)));
			}
		}

		envBuilder.prepare(_evaluator);
		//+ make environemnt
		Environment evalEnv = envBuilder.toEnvironment(_sym._id);
		//-
		// if Atom
		if (_sym instanceof SingleAtom) {
			return processAtom(evalEnv, _sym._id);
		} // if Compound
		else {
			return processCompound(evalEnv, _sym);
		}
	}

	private Product processAtom(Environment env, String id) throws EvaluationError {
		Set<ProductProperty> properties = env.evalMolecule(id);
		return new Product(id, properties);
	}

	private Product processCompound(Environment env, Molecule sym) throws EvaluationError {
	Set<Product> atomProductSet = new HashSet<Product>();
	//
	for (BondedAtom m : ((Compound) sym)._subAtoms) {
				atomProductSet.add(processAtom(env, m._id));
	}
			
			
			
			
		Set<ProductProperty> properties = env.evalMolecule(sym._id);
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
		return new Product(sym._id, properties);
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
