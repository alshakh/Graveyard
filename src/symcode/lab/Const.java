/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package symcode.lab;

import java.util.Objects;
import symcode.expr.EnvironmentPropertyList;
import symcode.expr.Expression;
import symcode.expr.Property;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public class Const {
	
	private final String _name;
	private final Expression _expression;
	private final Property _property;
	public Const(String name, Expression expression){
		_name = name;
		_expression = expression;
		_property = new Property(_name,_expression);
	}

	public String getName(){
		return _name;
	}
	public Expression getExpression(){
		return _expression;
	}

	public void addToEnvironmentPropertyList(EnvironmentPropertyList epl){
		if(epl.hasReference(_name)) return;
		epl.addReference(_name);
		//////////////////
		epl.addProperty(_property);
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 83 * hash + Objects.hashCode(this._name);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Const other = (Const) obj;
		if (!this._name.equals(other._name)) {
			return false;
		}
		return true;
	}


	
}
