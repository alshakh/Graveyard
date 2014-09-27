package symcode.lab;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;

/**
 * All classes in lab should fallow this abstract class.
 *
 * @author Ahmed Alshakh <ahmed.s.alshakh@gmail.com>
 */
public class Compound extends Molecule implements Loadable {
    /*
     This class extends matter, will have bonds and evaluation functions 
     */
    //

	/**
	 *
	 * @param id
	 * @param version
	 * @param constMap
	 * @param elementsSet
	 * @param bondMap
	 * @param references
	 */
	

    public Compound(String id, String version, HashMap constMap, HashSet elementsSet, BondExpr bond, HashSet<String> references) {
        super(id, version, constMap, elementsSet,bond, references);
    }

    public String toString(){
	    return "compound "+super.toString();
    }
}
