/**
 *      This program is free software: you can redistribute it and/or modify
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

/**
 * data structure to hold a messages for fails in file operations.
 * it returned by MediaFileList
 * @author Ahmed Alshakh <ahmed.s.alshakh@gmail.com>
 */
public class FailedFile {
    /**
     * file that caused the problem
     */
    private final String filePath;
    /**
     * error message
     */
    private final String msg;

    /**
     * constructor for class.
     * @param mediaFile
     * @param msg 
     */
    public FailedFile(String mediaFile, String msg) {
        this.filePath = mediaFile;
        this.msg = msg;
    }
    /**
     * access file name
     * @return file name
     */
    public String getFilePath() {
        return filePath;
    }
    /**
     * access error message.
     * @return error message
     */
    public String getMsg() {
        return msg;
    }
    
}
