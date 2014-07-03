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
package mp3organizer.core;

import java.io.File;
import static mp3organizer.core.Constants.defaultFields;
import mp3organizer.core.sortpattern.NotValidPatternException;
import mp3organizer.core.sortpattern.SortPattern;
import org.jaudiotagger.tag.FieldKey;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Ahmed Alshakh <ahmed.s.alshakh@gmail.com>
 */
public class MediaFileListTest {

    MediaFileList testFiles;

    public MediaFileListTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        testFiles = new MediaFileList();
        for (int i = 0; i < 50; i++) {
            try {
                testFiles.addFile(new MediaFile(FileOperations.copyFile(Constants.origTestFile, new File(Constants.TestTmpFolder + "/" + Constants.getRandomWord() + ".mp3"), true)));
            } catch (Exception ex) {
            }
        }
    }

    @After
    public void tearDown() {
        Constants.deleteTmpTestFile();
        testFiles = null;
    }

    //

    @Test
    public void changeFieldTest() {
        String name = Constants.getRandomWord();
        try {
            testFiles.setField(FieldKey.ARTIST, name);
        } catch (ProblemWithAudioFileException ex) {
            fail("Exception :" + ex.getMessage());
        }
        testFiles.commit();
        //

        assertEquals(testFiles.getField(FieldKey.ARTIST), name);
    }

    //

    @Test
    public void getFieldTest() {
        assertEquals(testFiles.getField(FieldKey.ARTIST), defaultFields.get(FieldKey.ARTIST));
    }

    //

    @Test
    public void sortTest() throws NotValidPatternException {
        SortPattern sp = new SortPattern("<artist>/<title>.EXT");
        //
        testFiles.sort(sp, Constants.TestTmpFolder);
        assertTrue(new File(Constants.TestTmpFolder + "/" + Constants.defaultFields.get(FieldKey.ARTIST) +"/"+
                            Constants.defaultFields.get(FieldKey.TITLE)+".mp3").exists());
    }
}
