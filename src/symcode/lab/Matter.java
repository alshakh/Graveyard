/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package symcode.lab;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import symcode.expr.Expression;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public abstract class Matter {
    /*
     This will include id,version, const and elements
     */

    private final String _name;
    //
    private final String _version;
    //
    private final HashMap<String, Expression> _const;
    //
    // map from id to molecule
    private final HashMap<String, Molecule> _elements;
    //
    //
    //
    
    public Matter(String _name,String _version, HashMap _const, HashSet _elements) {
        throw new UnsupportedOperationException();
    }

    //
    //
    public boolean hasConst(String constName) {
        throw new UnsupportedOperationException();
    }

    public double getConst(String constName) {
        throw new UnsupportedOperationException();
    }

    //
    //
    public String getName() {
        return _name;
    }

    public String getVersion() {
        return _version;
    }
    
}
