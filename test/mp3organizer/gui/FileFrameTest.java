/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mp3organizer.gui;

import java.awt.Robot;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.fest.swing.fixture.FrameFixture;
/**
 *
 * @author yousef-alsber
 */
public class FileFrameTest
{
    
    public FileFrameTest() {
    }
    
    private FrameFixture window;
   // private FrameFixture windowTag;
    private Robot robot;
       
    @Before
    public void setUp() 
    {
        FileFrame frame = new FileFrame();
        frame.addDummyFiles();
        window = new FrameFixture(frame);
        window.show();
    }
    
    @After
    public void tearDown() 
    {
        window.cleanUp();
    }

    @Test
    public void testChangeClick()
    { 
      window.button("changeButton").click();
    }
    
    @Test
    public void testSortClick()
    { 
      window.button("sortButton").click();
    }
    @Test
    public void testDeleteClick()
    { 
      window.button("removeButtton").click();
    }

}
