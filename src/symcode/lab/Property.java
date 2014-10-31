/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package symcode.lab;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import symcode.value.Doub;
import symcode.value.Str;
import symcode.value.Value;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public abstract class Property {

	public final Value _value;

	public Property(Value value) {
		_value = value;
	}
	
	public static boolean isSvgProperty(String propertyName){
		if(propertyName.equals("svg")){
			return true;
		}
		return false;
	}

	/**
	 * object members defined as having an id with dot in it. a.b
	 *
	 * @return
	 */
	public boolean isNormalProperty() {
		return (this.getClass().equals(NormalProperty.class));
	}

	public static class ProductProperty extends Property {

		public final String _propertyName;

		public ProductProperty(String propertyName, Value value) {
			super(value);
			_propertyName = propertyName;
		}

		public EvaluableProperty toEvaluableProperty(String underRef) {
			return new NormalProperty(underRef, _propertyName, _value);
		}
	}

	public static class ConstProperty extends Property implements EvaluableProperty {

		public final String _constName;

		public ConstProperty(String constName, Value value) {
			super(value);
			_constName = constName;
		}

		@Override
		public boolean needsJsObject() {
			return false;
		}

		/**
		 * returns the needed js object name. precondition:
		 * needsJsObject() == true
		 *
		 * @return
		 */
		@Override
		public String getJsObjectName() {
			return "";
		}

		@Override
		public Value getValue() {
			return _value;
		}

		@Override
		public String getPropertyName() {
			return _constName;
		}
	}

	public static class NormalProperty extends Property implements EvaluableProperty {
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
		public final String _moleculeId;
		public final String _propertyName;

		public NormalProperty(String moleculeId, String propertyName, Value value) {
			super(value);
			_moleculeId = moleculeId;
			_propertyName = propertyName;
		}

				@Override
		public Value getValue() {
			return _value;
		}

		@Override
		public boolean needsJsObject() {
			return true;
		}

		@Override
		public String getJsObjectName() {
			return _moleculeId;
		}

		@Override
		public String getPropertyName() {
			return _propertyName;
		}

		
	}

	public static interface EvaluableProperty {

		public boolean needsJsObject();

		/**
		 * returns the needed js object name. precondition:
		 * needsJsObject() == true
		 *
		 * @return
		 */
		public String getJsObjectName();

		public Value getValue();
		public String getPropertyName();
	}
	/*
	 public static class ExecutionProperty extends Property {

	 }
	 */
}
