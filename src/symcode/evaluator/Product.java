/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package symcode.evaluator;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import symcode.lab.Property;
import symcode.lab.Property.EvaluableProperty;
import symcode.lab.Property.NormalProperty;
import symcode.lab.Property.ProductProperty;
import symcode.lab.Util;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public class Product {
	public final String _id;
	public final Set<ProductProperty> _properties;

	public Product(String id, Set<ProductProperty> properties) {
		// TODO : check if properties has all Property.MANDATORY_PROPERTIES
		_id = id;
		_properties = Collections.unmodifiableSet(properties);
	}
	/**
	 *
	 * @param svg
	 */
	public Product(Set<ProductProperty> properties) {
		this(Util.generateRandomId(), properties);
	}

	public Set<EvaluableProperty> getEvaluablePropertySet(String underObjectRef) {
		Set<EvaluableProperty> ps = new HashSet<EvaluableProperty>();
		for(ProductProperty p : _properties){
			ps.add(new NormalProperty(underObjectRef, p._propertyName, p._value));
		}
		return ps;
	}
	public Property getProperty(String propertyName){
		for(ProductProperty p : _properties){
			if(p._propertyName.equals(propertyName)){
				return p;
			}
		}
		return null;
	}

	@Override
	public String toString(){
		return "product "+super.toString();
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 67 * hash + Objects.hashCode(this._properties);
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
		if (!Objects.equals(this._id, other._id)) {
			return false;
		}
		if(! _properties.equals(other._properties))
			return true;
		return true;
	}

}
