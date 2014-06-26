package mp3organizer.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import static mp3organizer.core.Constants.getRandomWord;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test if can work with files. copy, delete, move.
 *
 * @author Ahmed Alshakh <ahmed.s.alshakh@gmail.com>
 */
public class FileOperationsTest {

    public FileOperationsTest() {
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
    @Test
    public void isDirectoryEmpty_Empty() {
        File t = new File(Constants.TestTmpFolder+"/"+getRandomWord());
        File i = createRandomTreeOfDir(t);
        boolean n = FileOperations.isDirectoryEmpty(t);
        try {
            FileOperations.removeFile(t);
        } catch (IOException ex) {
            System.out.println("ERROR :Cannot remove test file");
        }
        assertTrue(n);
    }

    //
    @Test
    public void isDirectoryEmpty_notEmpty() {
        try {
            File t = new File(Constants.TestTmpFolder+"/"+getRandomWord());
            File i = createRandomTreeOfDir(t);
            File h = new File(i.getAbsoluteFile() + "/" + getRandomWord());
            Files.createFile(h.toPath());
            boolean n = FileOperations.isDirectoryEmpty(t);
            FileOperations.removeFile(t);
            assertFalse(n);
        } catch (IOException ex) {
            fail("Creation of files failed");
        }
    }
    /**
     * 
     * @param f
     * @return 
     */
    private File createRandomTreeOfDir(File f) {
        if (!f.exists()) {
            FileOperations.createDir(f);
        }
        File file;
        File returnfile = null;
        for (int r = 0; r < 3; r++) {
            file = new File(f.getPath() + "/" + getRandomWord());
            FileOperations.createDir(f);
            for (int t = 0; t < 3; t++) {
                returnfile = new File(file.getPath() + "/" + getRandomWord());
                FileOperations.createDir(returnfile);
            }
        }
        return returnfile;
    }
}