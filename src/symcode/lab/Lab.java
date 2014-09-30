
package symcode.lab;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * This class will handle the lab information and provide it to the processing class.
 * @author Ahmed Alshakh <ahmed.s.alshakh@gmail.com>
 */
public class Lab extends Template implements Loadable {

	/**
	 *
	 * @param id
	 * @param version
	 * @param constSet
	 * @param elementsSet
	 */
	public Lab(String id,String version, Set<Const> constSet, Set<Molecule> elementsSet)
	{
             super( id, version,  constSet,  elementsSet);
	}
	@Override
	public String toString(){
		return "lab "+super.toString();
	}
}