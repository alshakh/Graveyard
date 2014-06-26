
package mp3organizer.core;

/**
 *
 * @author Ahmed Alshakh <ahmed.s.alshakh@gmail.com>
 */
public class FailedFile {
    private final String filePath;
    private final String msg;

    public FailedFile(String mediaFile, String msg) {
        this.filePath = mediaFile;
        this.msg = msg;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getMsg() {
        return msg;
    }
    
}
