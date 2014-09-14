
package symcode.lab;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Ahmed Alshakh <ahmed.s.alshakh@gmail.com>
 */
 class Atom extends Molecule {

    public Atom(String id, String version, HashMap constMap, HashSet elementsSet, EnumMap bondMap) {
        super(id, version, constMap, elementsSet, bondMap);
    }
	
}
