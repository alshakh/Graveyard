
package symcode.lab;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * This class will handle the lab information and provide it to the processing class.
 * @author Ahmed Alshakh <ahmed.s.alshakh@gmail.com>
 */
public class Lab extends Template {

	/**
	 *
	 * @param id
	 * @param version
	 * @param propertySet
	 * @param constsProperties
	 * @param elementsSet
	 */
	public Lab(String id,String version, Set<Property> propertySet, Set<Property> constsProperties, Set<Molecule> elementsSet)
	{
             super( id, version,  propertySet, constsProperties, elementsSet);
	}
	@Override
	public String toString(){
		return "lab "+super.toString();
	}
}