
package symcode.lab;

import java.util.HashMap;
import java.util.HashSet;

/**
 * This class will handle the lab information and provide it to the processing 
 * class.
 * @author Ahmed Alshakh <ahmed.s.alshakh@gmail.com>
 */
public class Lab extends Matter {
	public Lab(String _name,String _version, HashMap _const, HashSet _elements)
	{
             super( _name, _version,  _const,  _elements);
             throw new UnsupportedOperationException();
	}
}