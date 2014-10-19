package symcode.lab;

import java.util.Set;

/**
 * All classes in lab should fallow this abstract class.
 *
 * @author Ahmed Alshakh <ahmed.s.alshakh@gmail.com>
 */
public class Compound extends Molecule implements Loadable {
    /*
     This class extends matter, will have bonds and evaluation functions 
     */

	/**
	 *
	 * @param id
	 * @param version
	 * @param elementsSet
	 * @param bond
	 * @param references
	 */
	

    public Compound(String id, String version, Set<Const> constSet, Set<Molecule> elementsSet, BondExpr bond, Set<String> references) {
        super(id, version, constSet, elementsSet,bond, references);
    }

    public String toString(){
	    return "compound "+super.toString();
    }

}
