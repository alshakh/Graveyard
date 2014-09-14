
package symcode.lab;

import java.util.HashMap;
import java.util.HashSet;

/**
 * This class will handle the lab information and provide it to the processing 
 * class.
 * @author Ahmed Alshakh <ahmed.s.alshakh@gmail.com>
 */
public class Lab extends Matter {
	public Lab(String id,String version, HashMap constMap, HashSet elementsSet)
	{
             super( id, version,  constMap,  elementsSet);
	}
}