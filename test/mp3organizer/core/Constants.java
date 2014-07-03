/*
    This program is free software: you can redistribute it and/or modify
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
import java.util.EnumMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jaudiotagger.tag.FieldKey;

/**
 *
 * @author Ahmed Alshakh <ahmed.s.alshakh@gmail.com>
 */
public class Constants {

    public static File origTestFile;
    public static File tmpTestFile;
    //
    public static File TestTmpFolder;
    //
    public static final EnumMap<FieldKey, String> defaultFields;

    static {
        TestTmpFolder = new File("tmp");
        origTestFile = new File("testFile.mp3");
        tmpTestFile = new File(TestTmpFolder.getAbsolutePath() +"/tmpTestFile.mp3");

        //
        defaultFields = new EnumMap<>(FieldKey.class);
        defaultFields.put(FieldKey.ARTIST, "artistTest");
        defaultFields.put(FieldKey.TITLE, "titleTest");
        defaultFields.put(FieldKey.ALBUM, "albumTest");
        defaultFields.put(FieldKey.YEAR, "1111");
    }

    //

    public static void deleteTmpTestFile() {
        try {
            FileOperations.removeFile(tmpTestFile);
        } catch (IOException ex) {
            System.out.println("ERROR : Cannot remove " + tmpTestFile + " : " + ex.getMessage());
        }
    }

    public static void copyTestFile() {
        try {
            FileOperations.copyFile(origTestFile, tmpTestFile,false);
        } catch (IOException ex) {
            System.out.println("ERROR : Cannot copy " + origTestFile + " : " + ex.getMessage());
        }
    }
    
    public static String getRandomWord(){
        StringBuilder str = new StringBuilder();
        for(int i=0 ; i<7 ; i++){
            str.append((char)('a'+Math.random()*('z'-'a')));
        }
        return str.toString();
    }
    
    public static void emptyTmpDir(){
        File[] f = TestTmpFolder.listFiles();
        for( File t : f){
            try {
                FileOperations.removeFile(t);
            } catch (IOException ex) {
                Logger.getLogger(Constants.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
