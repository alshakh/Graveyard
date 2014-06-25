package mp3organizer.core;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jaudiotagger.tag.FieldKey;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

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

    /**
     * Test of getField with ARTIST
     */
    @Test
    public void testGetField_ARTIST() {
        assertEquals(Constants.defaultFields.get(FieldKey.ARTIST)
                ,readField(FieldKey.ARTIST));
    }

    /**
     * Test of getField with ALBUM
     */
    @Test
    public void testGetField_ALBUM() {
        assertEquals(Constants.defaultFields.get(FieldKey.ALBUM)
                ,readField(FieldKey.ALBUM));
    }

    /**
     * Test of getField with TITLE
     */
    @Test
    public void testGetField_TITLE() {
        assertEquals(Constants.defaultFields.get(FieldKey.TITLE)
                ,readField(FieldKey.TITLE));
    }

    /**
     * Test of getField with TITLE
     */
    @Test
    public void testGetField_YEAR() {
        assertEquals(Constants.defaultFields.get(FieldKey.YEAR)
                ,readField(FieldKey.YEAR));
    }

    /**
     *
     * @param field
     * @return
     */
    public String readField(FieldKey field) {
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
