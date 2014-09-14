package symcode.lab;

import symcode.expr.Expression;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;

/**
 * All classes in lab should fallow this abstract class.
 *
 * @author Ahmed Alshakh <ahmed.s.alshakh@gmail.com>
 */
public abstract class Molecule extends Matter {
    /*
     This class extends matter, will have bonds and evaluation functions 
     */
    //

    private final EnumMap<Bond, Expression> _bond;

    //

    public Molecule(String id, String version, HashMap constMap, HashSet elementsSet, EnumMap bondMap) {
        super(id, version, constMap, elementsSet);
	this._bond = bondMap;
    }
    //
    public boolean hasBond() {
        throw new UnsupportedOperationException();
    }

    public Expression getBond(Bond bond) {
        throw new UnsupportedOperationException();
    }
    //
    //abstract OutputClass eval(HashMap<String,OutputClass> itomContents);
    //

}
