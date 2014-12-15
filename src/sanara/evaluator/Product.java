/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sanara.evaluator;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import sanara.lab.Property;
import sanara.lab.Property.NormalProperty;
import sanara.lab.Property.ProductProperty;
import sanara.lab.Util;

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

	public Set<Property> getEvaluablePropertySet(String underObjectRef) {
		Set<Property> ps = new HashSet<Property>();
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
}
