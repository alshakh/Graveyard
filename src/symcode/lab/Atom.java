
package symcode.lab;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Ahmed Alshakh <ahmed.s.alshakh@gmail.com>
 */
 class Atom extends Molecule {

    public Atom(String _name, String version, HashMap _const, HashSet _elements, EnumMap _bond) {
        super(_name, version, _const, _elements, _bond);
        throw new UnsupportedOperationException();
    }
	
}
