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
