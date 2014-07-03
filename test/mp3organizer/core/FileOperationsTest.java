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
        Constants.emptyTmpDir();
    }

    //
    @Test
    public void straightForwardCopy() {
        try {
            File f = new File(Constants.TestTmpFolder + "/" + getRandomWord() + "/" + getRandomWord()
                              + "/" + getRandomWord() + "/" + getRandomWord() + ".mp3");

            File t = FileOperations.copyFile(Constants.origTestFile, f, true);
            assertTrue(f.getAbsolutePath().equals(t.getAbsolutePath()) && f.exists());
        } catch (IOException ex) {
            fail("ERROR :Cannot copy test file");
        }
    }
    @Test
    public void copyToNonExistantDir() {
        try {
            File f = new File(Constants.TestTmpFolder + "/" + getRandomWord() + "/" + getRandomWord()
                              + "/" + getRandomWord() + "/" + getRandomWord() + ".mp3");

            File t = FileOperations.copyFile(Constants.origTestFile, f, true);
            assertTrue(f.getAbsolutePath().equals(t.getAbsolutePath()) && f.exists());
        } catch (IOException ex) {
            fail("ERROR :Cannot copy test file");
            
        }
    }
    @Test
    public void copyToAlreadyExistsFile() {
        try {
            File f = new File(Constants.TestTmpFolder + "/" + getRandomWord() + "/" + getRandomWord()
                              + "/" + getRandomWord() + "/" + getRandomWord() + ".mp3");
            FileOperations.createDir(f.getAbsoluteFile().getParentFile());
            Files.createFile(f.toPath());
            //
            File t = FileOperations.copyFile(Constants.origTestFile, f, true);
            System.out.println(t);
            assertTrue(f.exists() && t.exists() && !(f.getAbsolutePath().equals(t.getAbsolutePath())));
        } catch (IOException ex) {
            fail("ERROR :Cannot copy test file");
        }
    }

    @Test
    public void isDirectoryEmpty_Empty() {
        File t = new File(Constants.TestTmpFolder + "/" + getRandomWord());
        createRandomTreeOfDir(t);
        assertTrue(FileOperations.isDirectoryEmpty(t));
    }

    //
    @Test
    public void isDirectoryEmpty_notEmpty() {
        try {
            File t = new File(Constants.TestTmpFolder + "/" + getRandomWord());
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
    //
    
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
