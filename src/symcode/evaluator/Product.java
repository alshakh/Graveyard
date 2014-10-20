/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package symcode.evaluator;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import symcode.lab.Property;
import symcode.lab.Util;
import symcode.value.*;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public class Product {
	public final Doub _x;
	public final Doub _y;
	public final Doub _h;
	public final Doub _w;
	public final Svg _svg;
	public final String _id;
	/**
	 *
	 * @param svg
	 */
	public Product(String id, Svg svg, Doub x, Doub y, Doub h, Doub w) {
		_svg = svg;
		_x = x;
		_y = y;
		_h = h;
		_w = w;
		this._id = id;
	}
	/**
	 *
	 * @param svg
	 */
	public Product(Svg svg, Doub x, Doub y, Doub h, Doub w) {
		this(Util.generateRandomId(), svg, x, y, h, w);
	}

	public Set<Property> getEvaluablePropertySet(String underObjectRef) {
		Set<Property> ps = new HashSet<Property>();
		////////////////////
		ps.add(new Property(underObjectRef + "." + "x", _x ));
		ps.add(new Property(underObjectRef + "." + "y", _y ));
		ps.add(new Property(underObjectRef + "." + "h", _h ));
		ps.add(new Property(underObjectRef + "." + "w", _w ));
		ps.add(new Property(underObjectRef + "." + "svg",new Svg(toSvgString())));
		// TODO : better way to do svg
		//
		return ps;
	}
	//
	public String toSvgString() {
		// TODO : this code is not actual svg. do actural svg code
		return "<g x="+_x+" y="+_y+" h="+_h+" w="+_w+">\n" +
			_svg.toString() + "\n" +
			"</g>";
	}

	@Override
	public String toString(){
		return "product "+super.toString();
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 67 * hash + Objects.hashCode(this._x);
		hash = 67 * hash + Objects.hashCode(this._y);
		hash = 67 * hash + Objects.hashCode(this._h);
		hash = 67 * hash + Objects.hashCode(this._w);
		hash = 67 * hash + Objects.hashCode(this._svg);
		hash = 67 * hash + Objects.hashCode(this._id);
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
		final Product other = (Product) obj;
		if (!Objects.equals(this._x, other._x)) {
			return false;
		}
		if (!Objects.equals(this._y, other._y)) {
			return false;
		}
		if (!Objects.equals(this._h, other._h)) {
			return false;
		}
		if (!Objects.equals(this._w, other._w)) {
			return false;
		}
		if (!Objects.equals(this._svg, other._svg)) {
			return false;
		}
		if (!Objects.equals(this._id, other._id)) {
			return false;
		}
		return true;
	}


}
