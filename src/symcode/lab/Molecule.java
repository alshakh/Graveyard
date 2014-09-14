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

    public Molecule(String name, String version, HashMap _const, HashSet _elements, EnumMap _bond) {
        super(name, version, _const, _elements);
	this._bond = _bond;
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
