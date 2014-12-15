package sanara.lab.nativemolecule;

import java.util.HashSet;
import java.util.Set;
import sanara.lab.Property;
import sanara.lab.Property.NormalProperty;
import sanara.lab.SingleAtom;
import sanara.value.Doub;
import sanara.value.Str;

/**
 *
 * @author Ahmed Alshakh <ahmed.s.alshakh@gmail.com>
 */
public class EmptyAtom extends SingleAtom{
	
	public static final EmptyAtom INSTANCE = new EmptyAtom();
	public static final String ID = "EMPTY";
	//
	private EmptyAtom(){
		super("empty","",generateEmptyAtomProperties());
	}
	
	private static Set<Property> generateEmptyAtomProperties(){
		Set<Property> properties = new HashSet<Property>();
		properties.add(new NormalProperty(ID, "x",new Doub("0")));
		properties.add(new NormalProperty(ID, "y",new Doub("0")));
		properties.add(new NormalProperty(ID, "h",new Doub("100")));
		properties.add(new NormalProperty(ID, "w",new Doub("100")));
		properties.add(new NormalProperty(ID, "svg",new Str("<circle cx=\"50\" cy=\"50\" r=\"50\" stroke=\"black\" stroke-width=\"3\" fill=\"red\" />")));
		return properties;
	}
}
