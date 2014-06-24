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
     * Test of getField method, of class MediaFile.
     */
    @Test
    public void testGetField() {
        String result = "";
        try {
            result = new MediaFile(Constants.tmpTestFile).getField(FieldKey.ARTIST);
        } catch (Exception ex) {
            Logger.getLogger(MediaFileTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        assertEquals("artistTest", result);
    }
}
