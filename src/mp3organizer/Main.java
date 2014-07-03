package mp3organizer;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import mp3organizer.core.FileOperations;
import mp3organizer.gui.FileFrame;

/**
 *
 * @author Ahmed Alshakh <ahmed.s.alshakh@gmail.com>
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // new java.awt.FileDialog((java.awt.Frame) null).setVisible(true);
        try {
            // Set native look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            
        }
        FileFrame fileFrame = new FileFrame();
        fileFrame.setVisible(true);
    }
    
}
