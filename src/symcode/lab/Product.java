/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package symcode.lab;

import java.util.EnumMap;
import java.util.HashMap;
import symcode.expr.Expression;

/**
 * this class is the product of all the classes.
 * products cannot be nested, only combined.
 * @author Ahmed Alshakh www.alshakh.net
 */
public class Product implements Loadable, Evaluable {
	private final String _content;
	private final EnumMap<Bond,Expression> _bond;
	public Product(EnumMap<Bond,Expression> bondMap ,String content){
		this._content = content;
		this._bond = bondMap;
	}
	
	public String toSVG(){
		return _content;
	}

	@Override
	public Expression getBond(Bond b) {
		throw new UnsupportedOperationException(); 
	}

	@Override
	public String toString(){
		return "product: " + _content;
	}
	
	@Override
	public Product eval(HashMap<String, Product> input) {
		return eval();
	}

	@Override
	public Product eval() {
		return this;
	}
	
	public static Product combine(Product p1, Product p2){
		throw new UnsupportedOperationException();
	}
}