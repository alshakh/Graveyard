/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package symcode.lab;

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
    private final String _id;
    //
    private final String _version;
    //
    private final HashMap<String, Expression> _const;
    //
    // map from id to molecule
    private final HashSet<Molecule> _elements;
    //
    //
    //
    
    public Matter(String id,String version, HashMap constMap, HashSet elementsSet) {
	    this._const = constMap;
	    this._elements = elementsSet;
	    this._id = id;
	    this._version = version;
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
    public String getId() {
        return _id;
    }

    public String getVersion() {
        return _version;
    }
    //
    @Override
    public String toString(){
	    String SPACE = "    ";
	    String myRetStr = "";
	    myRetStr += "id: " + this._id;
	    //
	    String elementsRetStr = "";
	    boolean firstA = true;
	    for(Molecule m : _elements){
		    if(m==null) continue;
		    if(firstA){
			    firstA = false;
		    } else {
			    elementsRetStr +="\n";
		    }
		    //
		    String[] lines = m.toString().split("\n");
		    boolean firstB = true;
		    for(String l : lines){
			    if(firstB){
				    firstB = false;
			    }else {
				    elementsRetStr += "\n";
			    }
			    elementsRetStr += SPACE + l;
		    }
	    }
	    return myRetStr + (elementsRetStr.length()!=0?"\n"+elementsRetStr:"");
    }
    
}
