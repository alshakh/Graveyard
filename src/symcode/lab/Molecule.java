/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package symcode.lab;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import symcode.lab.Property.ConstProperty;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public abstract class Molecule extends Template {

	/**
	 *
	 * @param id
	 * @param version
	 * @param propertySet
	 */
	public Molecule(String id, String version, Set<Property> propertySet) {
		super(id, version, propertySet);
	}

	/**
	 * get the set properties needed to evaluate the molecule.
	 *
	 * @return
	 */
	public final Set<Property> getEvaluablePropertySet() {
		EvaluablePropertiesBuilder evaluableProperties = 
			new EvaluablePropertiesBuilder();
		insertPropertiesToBuilder(evaluableProperties);
		return evaluableProperties.getEvaluableProperties();
	}
	public void insertPropertiesToBuilder(EvaluablePropertiesBuilder builder){
		builder.addProperties(super._properties);
	}

	public static class EvaluablePropertiesBuilder {

		private final Set<Property> _currentProperties;

		public EvaluablePropertiesBuilder() {
			_currentProperties = new HashSet<Property>();
		}

		/**
		 * add property to the set considering property type and 
		 * duplications.
		 * @param property 
		 */
		public void addProperty(Property property) {
			/*
			check if constProperty is already defined
			*/
			if (property instanceof ConstProperty) {
				for (Property p : _currentProperties) {
					if (!(p instanceof ConstProperty)) {
						continue;
					}

					if (p._propertyName.equals(property._propertyName)) {
						return;
					}
				}
			}
			_currentProperties.add(property);
		}

		public void addProperties(Collection<Property> properties) {
			for(Property p : properties)
				addProperty(p);
		}

		public Set<Property> getEvaluableProperties(){
			return _currentProperties;
		}
	}
}