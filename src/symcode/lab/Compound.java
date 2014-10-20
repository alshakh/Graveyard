package symcode.lab;

import java.util.Set;

/**
 * All classes in lab should fallow this abstract class.
 *
 * @author Ahmed Alshakh <ahmed.s.alshakh@gmail.com>
 */
public class Compound extends Molecule {
    /*
     This class extends matter, will have bonds and evaluation functions 
     */

	/**
	 *
	 * @param id
	 * @param version
	 * @param propertySet
	 * @param elementsSet
	 * @param deps
	 */
	

    public Compound(String id, String version, Set<Property> propertySet, Set<Property> constsProperties, Set<Molecule> elementsSet, Set<String> deps) {
		super(id, version, propertySet,constsProperties, elementsSet, deps);
    }

    public String toString(){
	    return "compound "+super.toString();
    }

}
