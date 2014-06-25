/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mp3organizer.gui;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.fest.swing.fixture.FrameFixture;
//import org.fest.swing.testng.testcase.FestSwingJUnitTestCase;

/**
 *
 * @author yousef-alsber
 */
public class FileFrameTest {
    
    public FileFrameTest() {
    }
    
    private FrameFixture window;
    
    
 //   @BeforeClass 
 //   public void setUpOnce() 
 //   {
 //        FailOnThreadViolationRepaintManager.install();
 //   }
    
    @BeforeClass
    public static void setUpClass() 
    {
        
    }
    
    @AfterClass
    public static void tearDownClass() 
    {
        
    }
    
    @Before
    public void setUp() 
    {
        FileFrame fileFrame = new FileFrame();
        fileFrame.setVisible(true);
    }
    
    @After
    public void tearDown() 
    {
        window.cleanUp();
    }

    @Test
    public void testSomeMethod() 
    {
        
    }
    
}
