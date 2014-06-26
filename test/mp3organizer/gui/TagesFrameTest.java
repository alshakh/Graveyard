/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mp3organizer.gui;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher; 
import junit.framework.Assert;

import javax.swing.JLabel;
import org.fest.swing.finder.WindowFinder;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JComboBoxFixture;
import org.fest.swing.fixture.JLabelFixture;
import org.fest.swing.fixture.JTextComponentFixture;
import org.fest.swing.exception.ComponentLookupException;
import org.fest.swing.format.IntrospectionComponentFormatter;
import org.fest.swing.format.Formatting;
import org.fest.swing.core.GenericTypeMatcher;
import javax.swing.JButton;
import org.fest.swing.fixture.ContainerFixture;

/**
 *
 * @author yousef-alsber
 */
public class TagesFrameTest {
    
    public TagesFrameTest() 
    {
        
    }
    
    private FrameFixture window;
    
    @Before
    public void setUp() 
    {
        window = new FrameFixture(new TagesFrame());
        window.show();
    }
    
    @After
    public void tearDown() 
    {
        window.cleanUp();
    }

    @Test
    public void testCancelClick()
    { 
      window.button(new GenericTypeMatcher<JButton>() 
      {
         @Override protected boolean isMatching(JButton button) 
         {
                return "Cancel".equals(button.getText());
         } 
       });
    }
    public void testChangeClick()
    { 
      window.button(new GenericTypeMatcher<JButton>() 
      {
         @Override protected boolean isMatching(JButton button) 
         {
                return "Change".equals(button.getText());
         } 
       });
    }
}
