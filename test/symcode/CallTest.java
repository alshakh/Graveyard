/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package symcode;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import symcode.evaluator.EvalNode;
import symcode.evaluator.EvaluationError;
import symcode.evaluator.Evaluator;
import symcode.evaluator.Product;
import symcode.evaluator.SyntaxError;
import symcode.lab.Lab;
import symcode.lab.LabLoader;
import symcode.lab.Molecule;
import symcode.lab.Property;
import symcode.lab.Property.Call;
import symcode.lab.Property.NormalProperty;
import symcode.lab.SingleAtom;
import symcode.value.Doub;
import symcode.value.Expr;
import symcode.value.Str;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public class CallTest {
	public static Set<Property> simpleProperties;
	static {
		Set<Property> a = new HashSet<Property>();
		a.add(new Property.NormalProperty("test", "x", new Doub("11")));
		a.add(new Property.NormalProperty("test", "y", new Doub("22")));
		a.add(new Property.NormalProperty("test", "w", new Doub("33")));
		a.add(new Property.NormalProperty("test", "h", new Doub("44")));
		simpleProperties = Collections.unmodifiableSet(a);
	}
	public static Lab testLab = LabLoader.loadLab(new File("labs/testLab.json"));
	public static Evaluator testEvaluator = new Evaluator(testLab);
	////////////////////////
	////////////////////////
	////////////////////////
	public CallTest() {
	}
	
	@BeforeClass
	public static void setUpClass() {
	}
	
	@AfterClass
	public static void tearDownClass() {
	}
	
	@Before
	public void setUp() {
	}
	
	@After
	public void tearDown() {
	}
	
	@Test
	public void simpleCall() throws SyntaxError, EvaluationError {
		//fail();
		Product p = testEvaluator.eval("atomSimpleCall");
		Property property = getPropertyFromSet(p._properties, "y");
		assertTrue(113.0==property._value.toDouble());
	}
	
	@Test
	public void complexCall() throws SyntaxError, EvaluationError {
		Product p = testEvaluator.eval("atomComplexCall");
		
		Property property = getPropertyFromSet(p._properties, "svg");
		assertEquals("CALL114", property._value.toString());
	}
	
	public static Property getPropertyFromSet(Set<? extends Property> set, String pName){
		for(Property pr : set){
			if(pr._propertyName.equals(pName)) return pr;
		}
		return null;
	}
}