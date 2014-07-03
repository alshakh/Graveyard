
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
