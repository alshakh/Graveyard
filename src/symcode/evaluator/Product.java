/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package symcode.evaluator;

import java.util.HashSet;
import java.util.Set;
import symcode.expr.EnvironmentPropertyList;
import symcode.expr.Property;
import symcode.lab.Util;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public class Product {
	private final BondValue _bond;

	// 
	private final String _randId;
	/**
	 *
	 * @param bond
	 * @param svg
	 */
	public Product(BondValue bond) {
		this._bond = bond;
		this._randId = Util.generateRandomId();
	}

	
	public void addEnvironmentPropertyList(String underReference, EnvironmentPropertyList epl){
	}

	public Set<Property> evalPropertySet(){
		return evalPropertySet(_randId);
	}

	public Set<Property> evalPropertySet(String underReference){
		Set<Property> ps = new HashSet<Property>();
		////////////////////
		ps.add(new Property(underReference + "." + "x", _bond._x ));
		ps.add(new Property(underReference + "." + "y", _bond._y ));
		ps.add(new Property(underReference + "." + "h", _bond._h ));
		ps.add(new Property(underReference + "." + "w", _bond._w ));
		// TODO : add svg 
		return ps;
	}
	//

	public String toSvgString() {
		// TODO : this code is not actual svg. do actural svg code
		return "<g x="+_bond._x+" y="+_bond._y+" h="+_bond._h+" w="+_bond._w+">\n" +
			_bond._svg + "\n" +
			"</g>";
	}
	@Override
	public String toString(){
		return "product "+super.toString();
	}

	/**
	 *
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static Product combine(Product p1, Product p2){
		throw new UnsupportedOperationException();
	}
}
