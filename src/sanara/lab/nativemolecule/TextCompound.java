package sanara.lab.nativemolecule;

import sanara.lab.Compound;
import sanara.lab.Molecule;

/**
 *
 * @author Ahmed Alshakh <ahmed.s.alshakh@gmail.com>
 */
public class TextCompound extends Compound  {

	/**
	 *
	 * @param text
	 */
	public TextCompound(String text) {
		super("text", "",Molecule.EMPTY_PROPERTIES, Compound.EMPTY_ATOMS_SET);
		throw new UnsupportedOperationException("Not supported yet."); 
	}
	
}
