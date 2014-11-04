/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package symcode.value;

import java.util.HashSet;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public class ExprTest {
	
	public ExprTest() {
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
	public void testGetNeededProperties() {
		Expr instance = new Expr("2*x+2b2h+a.b2h");
		Set<String> result = instance.getNeededReferences();
		Set<String> expResult = new HashSet<String>();
		expResult.add("x");
		expResult.add("b2h");
		expResult.add("a.b2h");
		assertEquals(expResult, result);
	}

	/**
	 * Test of toString method, of class Expr.
	 */
	@Test
	public void testToString() {
		fail("The test case is a prototype.");
	}
	
}
