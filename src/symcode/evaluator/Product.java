/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package symcode.evaluator;

import java.util.EnumMap;
import symcode.lab.SvgString;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public class Product {
	private final BondValue _bond;
	private final SvgString _svg;

	//
	public Product(BondValue bond, SvgString svg) {
		this._bond = bond;
		this._svg = svg;
	}
	//
	public SvgString toSvgString(){
		throw new UnsupportedOperationException();
	}

	//
	@Override
	public String toString(){
		return "product "+super.toString();
	}

	//
	public static Product combine(Product p1, Product p2){
		throw new UnsupportedOperationException();
	}
}
