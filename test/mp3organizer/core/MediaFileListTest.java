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
