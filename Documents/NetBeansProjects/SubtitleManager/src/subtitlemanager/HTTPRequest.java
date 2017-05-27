/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package subtitlemanager;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.JOptionPane;

/**
 *
 * @atulgpt <atlgpt@gmail.com>
 */
public class HTTPRequest {

    private final String USER_AGENT = "SubDB/1.0 (atulgpt/0.1; https://github.com/atulgpt/SubtitleDownloader.git";
    private final String URL = "http://sandbox.thesubdb.com/";
    private SubtitleDownloaderUI mSubtitleDownloaderUI = null;

    public HTTPRequest() {
    }

    HTTPRequest(SubtitleDownloaderUI callingUI) {
        this();
        mSubtitleDownloaderUI = callingUI;
    }

    public ArrayList<String> sendRequest(ArrayList<String> videoHash, String lang) {
        ArrayList<String> subtitle = new ArrayList<>();
        try {
            subtitle = sendGet(videoHash, lang);
        } catch (Exception ex) {
            Logger.getLogger(HTTPRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return subtitle;
    }

    public String[] sendRequestForLangs() {
        return sendGetLangs().split(",");
    }

    // HTTP GET request
    private ArrayList<String> sendGet(ArrayList<String> videoHashArray, String lang) throws Exception {
        ArrayList<String> subtitleArray = new ArrayList<>();
        for (String videoHash : videoHashArray) {
            if (videoHash.equals("")) {
                subtitleArray.add("");
                continue;
            }
            String url = URL + "?action=download&hash=" + videoHash + "&language=" + lang;
            System.out.println("subtitlemanager.HTTPRequest.sendGet(): url- " + url);
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

            //add request header
            con.setRequestProperty("User-Agent", USER_AGENT);
            int responseCode;
            try {
                responseCode = con.getResponseCode();
            } catch (IOException e) {
                System.out.println("subtitlemanager.HTTPRequest.sendGet() error: " + e);
                subtitleArray.add("");
                if (mSubtitleDownloaderUI != null) {
                    mSubtitleDownloaderUI.showDialog("Network is not working!", "Message", JOptionPane.INFORMATION_MESSAGE);
                }
                continue;
            }
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);
            switch (responseCode) {
                case 200:
                    StringBuilder response;
                    try (BufferedReader in = new BufferedReader(
                            new InputStreamReader(con.getInputStream()))) {
                        String inputLine;
                        response = new StringBuilder();
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                            response.append("\n");
                        }
                        in.close();
                    }   //print result
                    //System.out.println("1:"+response.toString());
                    String subtitle = response.toString();
                    subtitleArray.add(subtitle);
                    break;
                case 404:
                    System.out.println("subtitlemanager.HTTPRequest.sendGet() Subtile not found, Response Code: " + responseCode);
                    if (mSubtitleDownloaderUI != null) {
                        mSubtitleDownloaderUI.showDialog("Subtitle not found!\n(Response code: " + responseCode + ")", "Message", JOptionPane.INFORMATION_MESSAGE);
                    }
                    subtitleArray.add("");
                    continue;
                case 402:

                    System.out.println("subtitlemanager.HTTPRequest.sendGet() Malformed request!\n(Response code: " + responseCode + ")");
                    if (mSubtitleDownloaderUI != null) {
                        mSubtitleDownloaderUI.showDialog("Malformed request!\n(Response code: " + responseCode + ")", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    subtitleArray.add("");
                default:
                    break;
            }
        }
        return subtitleArray;
    }

    // HTTP POST request
    private String sendPost() throws Exception {

        String url = "http://sandbox.thesubdb.com/";
        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";

        // Send post request
        con.setDoOutput(true);
        try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
            wr.writeBytes(urlParameters);
            wr.flush();
        }

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        StringBuffer response;
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }
        //print result
        //System.out.println(response.toString());
        return response.toString();
    }

    private String sendGetLangs() {
        ArrayList<String> langArray = new ArrayList<>();
        String url = URL + "?action=languages";
        System.out.println("subtitlemanager.HTTPRequest.sendGet(): url- " + url);
        URL obj = null;
        try {
            obj = new URL(url);
        } catch (MalformedURLException ex) {
            Logger.getLogger(HTTPRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (obj == null) {
            return "";
        }
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) obj.openConnection();
        } catch (IOException ex) {
            Logger.getLogger(HTTPRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (con == null) {
            return "";
        }
        try {
            // optional default is GET
            con.setRequestMethod("GET");
        } catch (ProtocolException ex) {
            Logger.getLogger(HTTPRequest.class.getName()).log(Level.SEVERE, null, ex);
        }

        //add request header
        int responseCode = -1;
        con.setRequestProperty("User-Agent", USER_AGENT);
        try {
            responseCode = con.getResponseCode();
        } catch (IOException ex) {
            Logger.getLogger(HTTPRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);
        switch (responseCode) {
            case 200:
                StringBuilder response = null;
                try (BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()))) {
                    String inputLine;
                    response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(HTTPRequest.class.getName()).log(Level.SEVERE, null, ex);
                }   //print result
                //System.out.println("1:"+response.toString());
                if (response == null) {
                    return "";
                }
                return response.toString();
            case 400:
                JOptionPane.showMessageDialog(null, "Malformed request!\n(Response code: " + responseCode + ")", "Error", JOptionPane.ERROR_MESSAGE);
                return "";
            default:
                return "";
        }
    }

}
