/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package subtitlemanager;


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
        String[] langArray = {"en", "es", "fr", "it", "nl", "pl", "pt", "ro", "sv", "tr"};
        SubtitleDownloaderUI subtitleDownloaderUI =  new SubtitleDownloaderUI(langArray);
        subtitleDownloaderUI.setVisible(true);
    }
}
