
package mp3organizer.gui;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.fest.swing.fixture.FrameFixture;

/**
 *
 * @author yousef-alsber
 */
public class SortFrameTest {
    
    public SortFrameTest() {
    }
    private FrameFixture window;
    
    @Before
    public void setUp() 
    {
        window = new FrameFixture(new SortFrame(null));
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
      window.button("cancelButton").click();
    }
    
    @Test
    public void testSortClick()
    { 
      window.button("sortFileButton").click();
    }
    
    @Test
    public void testTextField()
    {
        window.textBox("layoutText").enterText("<artist>/<year>/<album><title>.EXT");
        window.textBox("layoutText").requireText("<artist>/<year>/<album><title>.EXT");
    }
    
}
