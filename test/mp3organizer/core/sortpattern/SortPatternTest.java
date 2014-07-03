
package mp3organizer.core.sortpattern;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import mp3organizer.core.Constants;
import mp3organizer.core.FileNotChangeableException;
import mp3organizer.core.MediaFile;
import mp3organizer.core.ProblemWithAudioFileException;
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
public class SortPatternTest {
    
    public SortPatternTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    @Test(expected=NotValidPatternException.class)
    public void testInvalidInstantiation() throws NotValidPatternException{
        new SortPattern("asdjf lkjf ;lkadf ");
    }
    //
    @Test
    public void testValidInstantiation(){
        try {
            new SortPattern("<artist>.EXT");
        } catch (NotValidPatternException ex) {
            fail("Exception where it shouldn't");
        }
    }
    //
    @Test
    public void testNewPath() throws NotValidPatternException, FileNotChangeableException, FileNotFoundException, ProblemWithAudioFileException{
        SortPattern sp = new SortPattern("<artist>/<year>/<title>.EXT");
        File f = sp.proccessFilePath(new MediaFile(Constants.origTestFile), Constants.TestTmpFolder);
        assertEquals(f.getAbsolutePath(),Constants.TestTmpFolder.getAbsolutePath()+"/"+
                                         Constants.defaultFields.get(FieldKey.ARTIST) + "/" + 
                                         Constants.defaultFields.get(FieldKey.YEAR) + "/" + 
                                         Constants.defaultFields.get(FieldKey.TITLE)+".mp3");
    }
}
