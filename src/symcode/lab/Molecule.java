
package symcode.lab;

/**
 * All classes in lab should fallow this abstract class.
 * @author Ahmed Alshakh <ahmed.s.alshakh@gmail.com>
 */
public abstract class Molecule {
	public static int UNDEFINED = -5;
	//
	private final Const myConst;
	//
	private final float xBond;
	private final float yBond;
	private final float wBond;
	private final float hBond;
	//
	public Molecule(){
		myConst = null;
		xBond = UNDEFINED;
		yBond = UNDEFINED;
		wBond = UNDEFINED;
		hBond = UNDEFINED;
	}
	abstract boolean hasConst();
	abstract boolean hasThisConst();
}
