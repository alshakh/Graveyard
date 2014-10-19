/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package symcode.expr;

import java.util.Set;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public class Property {
	private final String _reference;
	private final Expression _expression;
	private final Set<String> _allDependencies;

	/**
	 *
	 * @param ref
	 * @param ex
	 */
	public Property(String ref, Expression ex){
		_reference = ref;
		_expression = ex;
		_allDependencies = _expression.extractReferences();
	}

	public Property(String ref, double d){
		// better way
		this(ref,new Expression(String.valueOf(d)));
	}

	/**
	 *
	 * @return
	 */
	public String getReference(){
		return _reference;
	}

	/**
	 *
	 * @return
	 */
	public Expression getExpression(){
		return _expression;
	}

	/**
	 *
	 * @return
	 */
	public Set<String> getDependencies(){
		return _allDependencies;
	}
	@Override
	public int hashCode() {
		int hash = 7;
		hash = 41 * hash + this._reference.hashCode();
		return hash;
	}
	/**
	 * same ref will be a duplication
	 * @param obj
	 * @return 
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Property other = (Property) obj;
		return this._reference.equals(other._reference);
	}

	/**
	 *
	 * @return
	 */
	public String toEnvironmentInput(){
		return _reference + "=" + _expression.toString();
	}
	public String toString(){
		return _reference + " : " + _expression.toString();
	}
}
