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
import java.lang.Object;
/**
 *
 * @author atulgupta
 */
public class VideoHashCalc {
    
    final int size = 1024*64;
    public String getHash(String videoFullName, String algo){
        return calHash(videoFullName, algo);
    }
    
    private String calHash(String videoFullName, String algo){
        byte[] videoBytes = null;
        byte[] videoBytesFinal = null;
        try {
            File videoFile = new File(videoFullName);
            videoBytes = Files.readAllBytes(videoFile.toPath());
            byte[] videoBytesFirst = Arrays.copyOfRange(videoBytes, 0, size);
            byte[] videoBytesLast = Arrays.copyOfRange(videoBytes,videoBytes.length - size , videoBytes.length);
            videoBytesFinal = new byte[2*size];
            System.arraycopy(videoBytesFirst, 0, videoBytesFinal, 0, videoBytesFirst.length);
            System.arraycopy(videoBytesLast,0, videoBytesFinal, videoBytesFirst.length, videoBytesLast.length);
        } catch (IOException e) {
             System.out.println("Error in IO: "+e);
        }
        String hashValue = "";
        try{
            MessageDigest messageDigest = MessageDigest.getInstance(algo);
            messageDigest.update(videoBytesFinal);
            byte[] digestedBytes = messageDigest.digest();
            hashValue = DatatypeConverter.printHexBinary(digestedBytes).toLowerCase();
        }catch(NoSuchAlgorithmException e){
            System.out.println("Error in hash: "+e);
        }
        System.out.println("subtitlemanager.VideoHashCalc.calHash(): hashValue- "+hashValue );
        return hashValue;
    }
}
