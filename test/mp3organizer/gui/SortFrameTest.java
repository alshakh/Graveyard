/**
 *     This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
