/*
 * Copyright (C) 2017 atulgpt <atlgpt@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package subtitlemanager;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.xml.bind.DatatypeConverter;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @atulgpt <atlgpt@gmail.com>
 */
public class VideoHashCalc {

    final int size = 1024 * 64;
    private SubtitleDownloaderUI subtitleDownloaderUI = null;

    public VideoHashCalc() {
    }

    VideoHashCalc(SubtitleDownloaderUI callingUI) {
        this();
        subtitleDownloaderUI = callingUI;
    }

    public ArrayList<String> getMD5Hash(String[] videoFilesName, String algo) {
        return calMD5Hash(videoFilesName, algo);
    }

    private ArrayList<String> calMD5Hash(String[] videoFilesName, String algo) {
        byte[] videoBytesFinal1;
        ArrayList<String> hashValue = new ArrayList<>();
        for (String videoFullName : videoFilesName) {
            try {
                System.out.println("subtitlemanager.VideoHashCalc.calHash()- Reading file: " + videoFullName);
                File videoFile = new File(videoFullName);
                if (videoFile.exists() && !videoFile.isDirectory()) {
                    byte[] videoBytesFirst1;
                    byte[] videoBytesLast1;
                    try (RandomAccessFile videoFile1 = new RandomAccessFile(videoFile, "r")) {
                        videoBytesFirst1 = new byte[size];
                        videoBytesLast1 = new byte[size];
                        videoFile1.read(videoBytesFirst1);
                        videoFile1.seek((int) videoFile1.length() - size);
                        videoFile1.read(videoBytesLast1);
                    }
                    videoBytesFinal1 = new byte[2 * size];
                    System.arraycopy(videoBytesFirst1, 0, videoBytesFinal1, 0, videoBytesFirst1.length);
                    System.arraycopy(videoBytesLast1, 0, videoBytesFinal1, videoBytesFirst1.length, videoBytesLast1.length);
                    if (videoBytesFinal1 != null) {
                        try {
                            MessageDigest messageDigest = MessageDigest.getInstance(algo);
                            messageDigest.update(videoBytesFinal1);
                            byte[] digestedBytes = messageDigest.digest();
                            String hash = DatatypeConverter.printHexBinary(digestedBytes).toLowerCase();
                            hashValue.add(hash);
                        } catch (NoSuchAlgorithmException e) {
                            System.out.println("Error in hash: " + e);
                            hashValue.add("");
                        }
                    } else {
                        hashValue.add("");
                    }
                } else {
                    System.out.println("Error: File path is wrong!");
                    SwingUtilities.invokeLater(() -> {
                        if (subtitleDownloaderUI != null) {
                            subtitleDownloaderUI.informUI("File path is wrong!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    });
                    hashValue.add("");
                }
            } catch (IOException e) {
                System.out.println("Error in reading file!: " + e);
                SwingUtilities.invokeLater(() -> {
                    if (subtitleDownloaderUI != null) {
                        subtitleDownloaderUI.informUI("Error in reading file!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                hashValue.add("");
            }

        }
        return hashValue;
    }
}
