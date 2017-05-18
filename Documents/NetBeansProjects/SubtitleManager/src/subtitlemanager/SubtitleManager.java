/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package subtitlemanager;

import java.lang.Object;
import java.io.*;
import java.util.*;

/**
 *
 * @author atulgupta
 */
public class SubtitleManager {

    /**
     * @param args the command line arguments
     */
    final static String hashAlgo = "MD5";

    public static void main(String[] args) {
        String[] langArray = {"en","other"};
        SubtitleDownloaderUI subtitleDownloaderUI =  new SubtitleDownloaderUI(langArray);
        subtitleDownloaderUI.setVisible(true);
    }
}
