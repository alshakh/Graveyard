package symcode.lab.nativemolecule;

import java.util.HashSet;
import java.util.Set;
import symcode.lab.Molecule;
import symcode.lab.Property;
import symcode.lab.Property.NormalProperty;
import symcode.lab.SingleAtom;
import symcode.lab.Template;
import symcode.value.Doub;
import symcode.value.Str;

/**
 *
 * @author Ahmed Alshakh <ahmed.s.alshakh@gmail.com>
 */
public class EmptyAtom extends SingleAtom{
	
	public static final EmptyAtom INSTANCE = new EmptyAtom();
	public static final String ID = "EMPTY";
	//
	private EmptyAtom(){
		super("empty","",generateEmptyAtomProperties() ,Template.EMPTY_CONSTS, Molecule.EMPTY_DEPENDENCIES);
	}
	
	private static Set<NormalProperty> generateEmptyAtomProperties(){
		Set<NormalProperty> properties = new HashSet<NormalProperty>();
		properties.add(new NormalProperty(ID, "x",new Doub("0")));
		properties.add(new NormalProperty(ID, "y",new Doub("0")));
		properties.add(new NormalProperty(ID, "h",new Doub("100")));
		properties.add(new NormalProperty(ID, "w",new Doub("100")));
		properties.add(new NormalProperty(ID, "svg",new Str("<circle cx=\"50\" cy=\"50\" r=\"50\" stroke=\"black\" stroke-width=\"3\" fill=\"red\" />")));
		return properties;
	}
}
