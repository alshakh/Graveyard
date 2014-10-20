/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package symcode.value;

import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ahmed Alshakh www.alshakh.net
 */
public class SvgTest {
	
	public SvgTest() {
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

	/**
	 * Test of getNeededProperties method, of class Svg.
	 */
	@Test
	public void testGetNeededProperties() {
		System.out.println("getNeededProperties");
		Svg instance = null;
		Set<String> expResult = null;
		Set<String> result = instance.getNeededProperties();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of toString method, of class Svg.
	 */
	@Test
	public void testToString() {
		System.out.println("toString");
		Svg instance = null;
		String expResult = "";
		String result = instance.toString();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of toEvaluableScript method, of class Svg.
	 */
	@Test
	public void testToEvaluableScript_1() {
		System.out.println("toEvaluableScript");
		Svg instance = new Svg("AAA <<< SS >>> BBB <<< QQ >>>");
		String expResult = "\"AAA \"+ SS +\" BBB \"+ QQ +\"\"";
		String result = instance.toEvaluableScript();
		assertEquals(expResult, result);
	}
	@Test
	public void testToEvaluableScript_2() {
		System.out.println("toEvaluableScript");
		Svg instance = new Svg("AAA <<< SS >>> BBB");
		String expResult = "\"AAA \"+ SS +\" BBB\"";
		String result = instance.toEvaluableScript();
		assertEquals(expResult, result);
	}
	
}
