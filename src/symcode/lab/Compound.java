package symcode.lab;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import symcode.expr.EnvironmentPropertyList;

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
	 * @param constMap
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

    /**
     * Does nothing. all things are taken care of by molecule
     * @param epl 
     */
	@Override
	public void addExtraToEnvironmentPropertyList(EnvironmentPropertyList epl) {
		return;
	}
}
