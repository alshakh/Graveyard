/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sanara.lab;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import sanara.value.Doub;
import sanara.value.Value;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public abstract class Property {

	public final Value _value;
	public final String _propertyName;
	
	public Property(String propertyName, Value value) {
		_value = value;
		_propertyName = propertyName;
	}
	
	/**
	 * 
	 */
	public static class ProductProperty extends Property {

		public ProductProperty(String propertyName, Value value) {
			super(propertyName, value);
		}

		public Property toEvaluableProperty(String underRef) {
			return new NormalProperty(underRef, _propertyName, _value);
		}
	}

	/**
	 * 
	 */
	public static class ConstProperty extends Property {
		public ConstProperty(String constName, Value value) {
			super(constName, value);
		}
	}

	public static class NormalProperty extends Property {
		public static final Map<String,Value> MANDATORY_PROPERTIES;
		static{
			Map m = new HashMap<String,Value>();
			m.put("x", new Doub("0"));
			m.put("y", new Doub("0"));
			m.put("w", new Doub("0"));
			m.put("h", new Doub("0"));
			MANDATORY_PROPERTIES = Collections.unmodifiableMap(m);
		}
		//
		//
		public final String _moleculeId;

		public NormalProperty(String moleculeId, String propertyName, Value value) {
			super(propertyName, value);
			_moleculeId = moleculeId;
		}
	}
	/**
	 * BackupProperties aren't used always they are only used when the 
	 *  property is not defined by other molecule.
	 * 
	 * Molecule1 wants $1 to have _property but $1 does not have _property
	 * so Molecule1 will have a BackupProperty as $1._property = $1.w+$1.h
	 * This property will be defined in the environment if _property is not 
	 * defined by $1
	 */
	public static class BackupNormalProperty extends NormalProperty {
		public BackupNormalProperty(String moleculeId, String propertyName, Value value) {
			super(moleculeId, propertyName, value);
		}
	}
	public static class BackupConstProperty extends ConstProperty {
		public BackupConstProperty(String propertyName, Value value) {
			super(propertyName, value);
		}
	}
	//
	//
	public static class Call extends Property {
		public Call(String id , Value expr){
			super(id,expr);
		}
	}
}