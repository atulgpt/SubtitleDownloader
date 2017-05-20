/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package subtitlemanager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.xml.bind.DatatypeConverter;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @atulgpt <atlgpt@gmail.com>
 */
public class VideoHashCalc {

    final int size = 1024 * 64;

    public ArrayList<String> getHash(String[] videoFilesName, String algo) {
        return calHash(videoFilesName, algo);
    }

    private ArrayList<String> calHash(String[] videoFilesName, String algo) {
        byte[] videoBytes = null;
        byte[] videoBytesFinal = null;
        ArrayList<String> hashValue = new ArrayList<>();
        for (String videoFullName : videoFilesName) {
            try {
                File videoFile = new File(videoFullName);
                if (videoFile.exists() && !videoFile.isDirectory()) {
                    videoBytes = Files.readAllBytes(videoFile.toPath());
                    byte[] videoBytesFirst = Arrays.copyOfRange(videoBytes, 0, size);
                    byte[] videoBytesLast = Arrays.copyOfRange(videoBytes, videoBytes.length - size, videoBytes.length);
                    videoBytesFinal = new byte[2 * size];
                    System.arraycopy(videoBytesFirst, 0, videoBytesFinal, 0, videoBytesFirst.length);
                    System.arraycopy(videoBytesLast, 0, videoBytesFinal, videoBytesFirst.length, videoBytesLast.length);
                } else {
                    JOptionPane.showMessageDialog(null, "File path is wrong!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException e) {
                System.out.println("Error in IO: " + e);
                JOptionPane.showMessageDialog(null, "Error in reading file!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            if (videoBytes != null) {
                try {
                    MessageDigest messageDigest = MessageDigest.getInstance(algo);
                    messageDigest.update(videoBytesFinal);
                    byte[] digestedBytes = messageDigest.digest();
                    String hash = DatatypeConverter.printHexBinary(digestedBytes).toLowerCase();
                    hashValue.add(hash);
                } catch (NoSuchAlgorithmException e) {
                    System.out.println("Error in hash: " + e);
                }
            } else {
            }
        }
        return hashValue;
    }
}
