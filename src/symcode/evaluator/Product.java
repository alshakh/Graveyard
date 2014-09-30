/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package symcode.evaluator;

import java.util.EnumMap;
import symcode.expr.EnvironmentPropertyList;
import symcode.expr.Property;
import symcode.lab.SvgString;
import symcode.lab.Util;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public class Product {
	private final BondValue _bond;
	private final SvgString _svg;

	// 
	private final String _randId;
	/**
	 *
	 * @param bond
	 * @param svg
	 */
	public Product(BondValue bond, SvgString svg) {
		this._bond = bond;
		this._svg = svg;
		this._randId = Util.generateRandomId();
	}

	/**
	 *
	 * @return
	 */
	public SvgString toSvgString(){
		throw new UnsupportedOperationException();
	}
	
	public void addEnvironmentPropertyList(String underReference, EnvironmentPropertyList epl){
		if(epl.hasReference(underReference))
			return;
		epl.addReference(underReference);
		////////////////////
		epl.addProperty(new Property(underReference + "." + "x", _bond._x ));
		epl.addProperty(new Property(underReference + "." + "y", _bond._y ));
		epl.addProperty(new Property(underReference + "." + "h", _bond._h ));
		epl.addProperty(new Property(underReference + "." + "w", _bond._w ));
		// TODO : add your svg string
	}

	public void addEnvironmentPropertyList(EnvironmentPropertyList epl){
		addEnvironmentPropertyList(_randId,epl);
	}
	//

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
