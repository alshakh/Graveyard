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
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static mp3organizer.core.Constants.TestTmpFolder;
import org.jaudiotagger.tag.FieldKey;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Ahmed Alshakh <ahmed.s.alshakh@gmail.com>
 */
public class MediaFileTest {

    public MediaFileTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        Constants.deleteTmpTestFile();
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        Constants.copyTestFile();

    }

    @After
    public void tearDown() {
        Constants.deleteTmpTestFile();
    }
    
    
    @Test
    public void testMoveFile(){
        try {
            new MediaFile(Constants.tmpTestFile).moveFile(new File(TestTmpFolder.getAbsolutePath()+"/aaa/bbb/ccc/d.mp3"));
        } catch (Exception ex) {
            fail("exception");
        }
        assertTrue(new File(TestTmpFolder.getAbsoluteFile()+"/aaa/bbb/ccc/d.mp3").exists());
    }
    //
    @Test
    public void testGetField_ARTIST() {
        assertEquals(Constants.defaultFields.get(FieldKey.ARTIST)
                ,readFieldOfTmpTestFile(FieldKey.ARTIST));
    }
    @Test
    public void testGetField_YEAR() {
        assertEquals(Constants.defaultFields.get(FieldKey.YEAR)
                ,readFieldOfTmpTestFile(FieldKey.YEAR));
    }
    @Test
    public void testGetField_ALBUM() {
        assertEquals(Constants.defaultFields.get(FieldKey.ALBUM)
                ,readFieldOfTmpTestFile(FieldKey.ALBUM));
    }
    @Test
    public void testSetField_ARTIST() throws Exception {
        assertTrue(setFieldTest(FieldKey.ARTIST));
    }
    @Test
    public void testSetField_ALBUM() throws Exception {
        assertTrue(setFieldTest(FieldKey.ALBUM));
    }
    @Test
    public void testSetField_YEAR() throws Exception {
        assertTrue(setFieldTest(FieldKey.YEAR));
    }
    @Test
    public void testSetField_TITLE() throws Exception {
        assertTrue(setFieldTest(FieldKey.TITLE));
    }
    @Test
    public void testSetField_MultipleFields() throws Exception {
        MediaFile mf = new MediaFile(Constants.tmpTestFile);
        String f1 = Constants.getRandomWord();
        String f2 = Constants.getRandomWord();
        mf.setField(FieldKey.ARTIST, f1);
        mf.setField(FieldKey.ALBUM, f2);
        mf.commit();
        MediaFile mff = new MediaFile(Constants.tmpTestFile);
        assertEquals(mff.getField(FieldKey.ARTIST)+mff.getField(FieldKey.ALBUM)
                ,f1+f2);
       
    }
    
    public boolean setFieldTest(FieldKey field) throws Exception {
        MediaFile mf = new MediaFile(Constants.tmpTestFile);
        String newFieldValue = Constants.getRandomWord();
        mf.setField(field, newFieldValue);
        mf.commit();
        MediaFile mff = new MediaFile(Constants.tmpTestFile);
        return mff.getField(field).equals(newFieldValue);
    }

    /**
     * Test of getField with TITLE
     */
    @Test
    public void testGetField_TITLE() {
        assertEquals(Constants.defaultFields.get(FieldKey.TITLE)
                ,readFieldOfTmpTestFile(FieldKey.TITLE));
    }


    /**
     * 
     * @param field
     * @return
     */
    public String readFieldOfTmpTestFile(FieldKey field) {
        String result;
        try {
            result = new MediaFile(Constants.tmpTestFile).getField(field);
        } catch (Exception ex) {
            Logger.getLogger(MediaFileTest.class.getName()).log(Level.SEVERE, null, ex);
            return "CANNOT READ";
        }
        return result;
    }
}