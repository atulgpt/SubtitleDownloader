/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package subtitlemanager;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @atulgpt <atlgpt@gmail.com>
 */
public class SubtitleManager {

    /**
     * @param args the command line arguments
     */
    final static String HASH_ALGO = "MD5";

    public static void main(String[] args) {
        if (UserPreferences.getLookAndFeel() == UserPreferences.SYS_LOOK_AND_FEEL) {
            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName());
                UIManager.put("FileChooser.useSystemExtensionHiding", false); 
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                Logger.getLogger(SubtitleManager.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error in changing LnF: "+ex);
            }
        } else if (UserPreferences.getLookAndFeel() == UserPreferences.JAVA_LOOK_AND_FEEL) {
            try {
                UIManager.setLookAndFeel(
                        UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                Logger.getLogger(SubtitleManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        String[] langArray = {"en", "es", "fr", "it", "nl", "pl", "pt", "ro", "sv", "tr"};
        SubtitleDownloaderUI subtitleDownloaderUI = new SubtitleDownloaderUI(langArray);
        subtitleDownloaderUI.setVisible(true);
    }
}
