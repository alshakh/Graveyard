

package mp3organizer.gui;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.fest.swing.fixture.FrameFixture;

/**
 *
 * @author yousef-alsber
 */
public class TagsFrameTest {
    
    public TagsFrameTest() 
    {
        
    }
    
    private FrameFixture window;
    
    @Before
    public void setUp() 
    {
        window = new FrameFixture(new TagsFrame(null));
        window.show();
    }
    
    @After
    public void tearDown() 
    {
        
    }
    
    @Test
    public void testCancelClick()
    { 
      window.button("cancelButton").click();
    }
}
