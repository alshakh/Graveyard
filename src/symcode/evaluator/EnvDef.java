/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package symcode.evaluator;

import java.util.Set;
import symcode.evaluator.EnvironmentBuilder.EnvSortable;
import symcode.lab.Property;
import symcode.lab.Property.BackupConstProperty;
import symcode.lab.Property.BackupNormalProperty;
import symcode.lab.Property.Call;
import symcode.lab.Property.ConstProperty;
import symcode.lab.Property.NormalProperty;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public abstract class EnvDef implements EnvSortable {

	public final String _reference;
	public final Property _property;

	public EnvDef(Property property, String reference) {
		_property = property;
		_reference = reference;
	}
	
	public abstract boolean needsJsObject();
	public abstract String getJsObjectName();
	
	@Override
	public Set<String> getDependencies() {
		return _property._value.getNeededReferences();
	}
	
	@Override
	public boolean matchReference(String reference) {
		return reference.equals(_reference);
	}
	
	@Override
	public String getReference() {
		return _reference;
	}

	public String toEvaluableScript() {
		return _reference + " = " + _property._value.toEvaluableScript();
	}
	/**
	 *
	 */
	public static class CallEnvDef extends EnvDef {

		public CallEnvDef(Property property) {
			super(property, property._propertyName);
			if (!(property instanceof Call)) {
				throw new IllegalArgumentException("Only accepts Call Property");
			}
		}

		@Override
		public boolean needsJsObject() {
			throw new UnsupportedOperationException();
		}

		@Override
		public String getJsObjectName() {
			throw new UnsupportedOperationException();
		}

		@Override
		public String toEvaluableScript() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean matchReference(String reference) {
			if (reference.indexOf('.') >= 0) {
				return reference.substring(0, reference.indexOf('.')).equals(super._reference);
			} else {
				return reference.equals(super._reference);
			}
		}
	}

	/**
	 *
	 */
	public static class NormalEnvDef extends EnvDef {

		public NormalEnvDef(Property property) {
			super(property, constructReference(property));
			if (!(property instanceof NormalProperty)) {
				throw new IllegalArgumentException("Only accepts normal properties");
			}
		}

		private static String constructReference(Property p) {
			if (p instanceof NormalProperty) {
				NormalProperty np = (NormalProperty) p;
				return np._moleculeId + "." + np._propertyName;
			}
			return "";
		}

		@Override
		public boolean needsJsObject() {
			return true;
		}

		@Override
		public String getJsObjectName() {
			return ((NormalProperty) super._property)._moleculeId;
		}
	}

	/**
	 * 
	 */
	public static class ConstEnvDef extends EnvDef {

		public ConstEnvDef(Property property) {
			super(property, constructReference(property));
			if (!(property instanceof ConstProperty)) {
				throw new IllegalArgumentException("Only accepts normal properties");
			}
		}

		public static String constructReference(Property p) {
			if (p instanceof ConstProperty) {
				return p._propertyName;
			}
			return "";
		}

		@Override
		public boolean needsJsObject() {
			return false;
		}

		@Override
		public String getJsObjectName() {
			throw new UnsupportedOperationException("Const Property, no jsObject");
		}
	}

	/**
	 *
	 */
	public static abstract class BackupEnvDef extends EnvDef {

		public BackupEnvDef(Property property, String reference) {
			super(property, reference);
		}
	}

	/**
	 * 
	 */
	public static class BackupNormalEnvDef extends BackupEnvDef {

		public BackupNormalEnvDef(Property property) {
			super(property, NormalEnvDef.constructReference(property));
			if (!(property instanceof BackupNormalProperty)) {
				throw new IllegalArgumentException("Only accepts backupNormalProperty");
			}
		}

		@Override
		public boolean needsJsObject() {
			return true;
		}

		@Override
		public String getJsObjectName() {
			return ((NormalProperty) _property)._moleculeId;
		}
	}

	/**
	 * 
	 */
	public static class BackupConstEnvDef extends BackupEnvDef {

		public BackupConstEnvDef(Property property) {
			super(property, ConstEnvDef.constructReference(property));
			if (!(property instanceof BackupConstProperty)) {
				throw new IllegalArgumentException("Only accepts backupConstProperty");
			}
		}

		@Override
		public boolean needsJsObject() {
			return false;
		}

		@Override
		public String getJsObjectName() {
			throw new UnsupportedOperationException(" no js object for backup const property");
		}
	}

	/**
	 * Creates an EnvDef from a property.
	 */
	public static class EnvDefFactory {

		public static EnvDef create(Property property) {
			/*
			 must check backupNormalProperty and 
			 backupConstProperty before checing constProperty
			 and NormalProperty because backupNormalProperty 
			 is instance of NormalProeprty and it will match
			 and the same thing with backupConstProperty
			 */
			if (property instanceof BackupNormalProperty) {
				return new BackupNormalEnvDef(property);
			} else if (property instanceof BackupConstProperty) {
				return new BackupConstEnvDef(property);
			} else if (property instanceof NormalProperty) {
				return new NormalEnvDef(property);
			} else if (property instanceof ConstProperty) {
				return new ConstEnvDef(property);
			} else if (property instanceof Call) {
				return new CallEnvDef(property);
			}
			throw new IllegalArgumentException("Illegal property type");
		}
	}
}
