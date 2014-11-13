/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package symcode.evaluator;

import java.util.Set;
import symcode.lab.Property;
import symcode.lab.Property.Call;
import symcode.lab.Property.ConstProperty;
import symcode.lab.Property.NormalProperty;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public abstract class EnvDef {

	public final String _reference;
	public final Property _property;
	public EnvDef(Property property, String reference) {
		_property = property;
		_reference = reference;
	}
	public Set<String> getDependencies(){
		return _property._value.getNeededReferences();
	}

	public abstract boolean needsJsObject();

	public abstract String getJsObjectName();

	public String toEvaluableScript() {
			return _reference + " = "+ _property._value.toEvaluableScript();
		}
	/**
	 *
	 */
	public static class CallEnvDef extends EnvDef {
		public CallEnvDef(Property call) {
			super(call, call._propertyName);
		}

		@Override
		public boolean needsJsObject() {
			throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
		}

		@Override
		public String getJsObjectName() {
			throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
		}

		@Override
		public String toEvaluableScript() {
			throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
		}
	}
	/**
	 *
	 */
	public static class NormalEnvDef extends EnvDef {
		public NormalEnvDef(Property property) {
			super(property, constructReference(property));
			if(!(property instanceof NormalProperty)) 
				throw new IllegalArgumentException("Only accepts normal properties");
		}
		private static String constructReference(Property p){
			if(p instanceof NormalProperty){
				NormalProperty np = (NormalProperty)p;
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
			return ((NormalProperty)super._property)._moleculeId;
		}

		
	}
	public static class ConstEnvDef extends EnvDef {
		public ConstEnvDef(Property property) {
			super(property, constructReference(property));
			if(!(property instanceof ConstProperty)) 
				throw new IllegalArgumentException("Only accepts normal properties");
		}
		public static String constructReference(Property p){
			if(p instanceof ConstProperty){
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
	public static class BackupEnvDef extends EnvDef {

		public BackupEnvDef(Property property, String reference) {
			super(property, reference);
		}

		@Override
		public boolean needsJsObject() {
			throw new UnsupportedOperationException("NO JS FOR BACKUP."); 
		}

		@Override
		public String getJsObjectName() {
			throw new UnsupportedOperationException("NO JS FOR BACKUP."); 
		}
	}
	public static class BackupNormalEnvDef extends BackupEnvDef {
		public BackupNormalEnvDef(Property property){
			super(property, NormalEnvDef.constructReference(property));
		}
	}
	public static class BackupConstEnvDef extends BackupEnvDef {
		public BackupConstEnvDef(Property property){
			super(property, ConstEnvDef.constructReference(property));
		}
	}
}
