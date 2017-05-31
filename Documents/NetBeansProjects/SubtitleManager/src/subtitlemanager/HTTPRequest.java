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

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @atulgpt <atlgpt@gmail.com>
 */
public class HTTPRequest {

    private final String USER_AGENT = "SubDB/1.0 (atulgpt/0.1; https://github.com/atulgpt/SubtitleDownloader.git";
    private final String API = "http://api.thesubdb.com/";
    private final String SANDBOX_API = "http://sandbox.thesubdb.com/";
    private final String URL = SANDBOX_API;
    private SubtitleDownloaderUI subtitleDownloaderUI = null;

    public HTTPRequest() {
    }

    HTTPRequest(SubtitleDownloaderUI callingUI) {
        this();
        subtitleDownloaderUI = callingUI;
    }

    public ArrayList<String> sendDownloadRequests(ArrayList<String> videoHashArray, String lang) {
        ArrayList<String> subtitleArray = new ArrayList<>();
        videoHashArray.forEach((videoHash) -> {
            try {
                if (!videoHash.equals("")) {
                    String subtitle = sendGetSubtitle(videoHash, lang);
                    subtitleArray.add(subtitle);
                } else {
                    subtitleArray.add("");
                }
            } catch (Exception ex) {
                Logger.getLogger(HTTPRequest.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        return subtitleArray;
    }

    void sendUploadRequests(ArrayList<String> videoHashArray, ArrayList<String> mSubtitleFilePathArray, String mLangs) {
        for (int i = 0; i < videoHashArray.size(); i++) {
            try {
                sendPostSubtitle(videoHashArray.get(i), mSubtitleFilePathArray.get(i));

            } catch (Exception ex) {
                Logger.getLogger(HTTPRequest.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public String[] sendRequestForLangs() {
        return sendGetLangs().split(",");
    }

    // HTTP GET request
    private String sendGetSubtitle(String videoHash, String lang) throws Exception {
        String url = URL + "?action=download&hash=" + videoHash + "&language=" + lang;
        System.out.println("subtitlemanager.HTTPRequest.sendGet(): url- " + url);
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode;
        try {
            responseCode = con.getResponseCode();
        } catch (IOException e) {
            System.out.println("subtitlemanager.HTTPRequest.sendGet() Error: " + e);
            SwingUtilities.invokeLater(() -> {
                if (subtitleDownloaderUI != null) {
                    subtitleDownloaderUI.informUI("Network is not working!\n Process interrupted. Try again!", "Message", JOptionPane.INFORMATION_MESSAGE);
                }
            });
            return "";
        }
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
                }
                return response.toString();
            case 404:
                System.out.println("subtitlemanager.HTTPRequest.sendGet() Subtile not found, Response Code: " + responseCode);
                SwingUtilities.invokeLater(() -> {
                    if (subtitleDownloaderUI != null) {
                        subtitleDownloaderUI.informUI("Subtitle not found!\n(Response code: " + responseCode + ")", "Message", JOptionPane.INFORMATION_MESSAGE);
                    }
        });
                return "";
            case 402:
                System.out.println("subtitlemanager.HTTPRequest.sendGet() Malformed request!\n(Response code: " + responseCode + ")");
                SwingUtilities.invokeLater(() -> {
                    if (subtitleDownloaderUI != null) {
                        subtitleDownloaderUI.informUI("Malformed request!\n(Response code: " + responseCode + ")", "Error", JOptionPane.ERROR_MESSAGE);
                    }
        });
                return "";
            default:
                break;
        }
        return "";
    }

    // HTTP POST request
    private String sendPostSubtitle(String videoHash, String subtitlePath) throws Exception {
        String url = URL + "?action=upload&hash=" + videoHash;
        String boundary = "xYzZY";
        String LINE_FEED = "\r\n";
        File subtitleFile = new File(subtitlePath);
        long dataLength = subtitleFile.length();
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
        con.setRequestProperty("Content-Length", Long.toString(dataLength));
        // Send post request
        con.setDoOutput(true);
        try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {

            StringBuilder temp = new StringBuilder();
            wr.writeBytes("--" + boundary + LINE_FEED);
            temp.append("--").append(boundary).append(LINE_FEED);
            wr.writeBytes("Content-Disposition: form-data; name=" + "\"hash\"" + LINE_FEED);
            wr.writeBytes(LINE_FEED);
            temp.append("Content-Disposition: form-data; name=\"hash\"").append(LINE_FEED);
            temp.append(LINE_FEED);
            wr.writeBytes(videoHash + LINE_FEED);
            temp.append(videoHash).append(LINE_FEED);
            wr.writeBytes("--" + boundary + LINE_FEED);
            temp.append("--").append(boundary).append(LINE_FEED);
            wr.writeBytes("Content-Disposition: form-data; name=" + "\"file\"; " + "filename=" + "\"subtitle.srt\"" + LINE_FEED);
            temp.append("Content-Disposition: form-data; name=\"file\"; filename=\"").append(videoHash).append(".srt\"").append(LINE_FEED);
            wr.writeBytes("Content-Type: application/octet-stream");
            temp.append("Content-Type: application/octet-stream");
            wr.writeBytes(LINE_FEED);
            wr.flush();
            temp.append(LINE_FEED);
            wr.write(Files.readAllBytes(Paths.get(subtitlePath)));
            wr.writeBytes(LINE_FEED);
            temp.append(Arrays.toString(Files.readAllBytes(Paths.get(subtitlePath)))).append("\n len=").append(Files.readAllBytes(Paths.get(subtitlePath)).length);
            //temp.append(LINE_FEED);
            //wr.writeBytes("--"+boundary+LINE_FEED);
            //temp.append("--").append(boundary).append(LINE_FEED);
            wr.flush();
            System.out.println("check: \n" + temp.toString());
        }

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + url);
        System.out.println("Response Code : " + responseCode);
        System.out.println("dataLength: " + Long.toString(dataLength));

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
        System.out.println(response.toString());
        return response.toString();
    }

    private String sendGetLangs() {
        ArrayList<String> langArray = new ArrayList<>();
        String url = URL + "?action=languages";
        System.out.println("subtitlemanager.HTTPRequest.sendGet():get request for url- " + url);
        URL obj = null;
        try {
            obj = new URL(url);

        } catch (MalformedURLException ex) {
            Logger.getLogger(HTTPRequest.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        if (obj == null) {
            return "";
        }
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) obj.openConnection();

        } catch (IOException ex) {
            Logger.getLogger(HTTPRequest.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        if (con == null) {
            return "";
        }
        try {
            // optional default is GET
            con.setRequestMethod("GET");

        } catch (ProtocolException ex) {
            Logger.getLogger(HTTPRequest.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        //add request header
        int responseCode = -1;
        con.setRequestProperty("User-Agent", USER_AGENT);
        try {
            responseCode = con.getResponseCode();

        } catch (IOException ex) {
            Logger.getLogger(HTTPRequest.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
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
                    Logger.getLogger(HTTPRequest.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
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
