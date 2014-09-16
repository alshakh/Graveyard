
package symcode;

import symcode.expr.ExprEnvironment;
import symcode.expr.Expression;

/**
 *
 * @author Ahmed Alshakh <ahmed.s.alshakh@gmail.com>
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

	    //Expression a = new Expression("2*x.a");
	    //System.out.println(a.eval(new ExprEnvironment("var x = { a:23}")));
        System.out.print(symcode.lab.LabLoader.loadLab(new java.io.File("labs/demoLab.json")).toString());
    }
    
}
