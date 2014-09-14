
package symcode.lab;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Ahmed Alshakh <ahmed.s.alshakh@gmail.com>
 */
class Compound extends Molecule {

    public Compound(String id, String version, HashMap constMap, HashSet elementsSet, EnumMap bondMap) {
        super(id, version, constMap, elementsSet, bondMap);
    }
	
}
