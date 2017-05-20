/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package subtitlemanager;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;

/**
 *
 * @atulgpt <atlgpt@gmail.com>
 */
public class HTTPRequest {

    private final String USER_AGENT = "SubDB/1.0 (Pyrrot/0.1; http://github.com/jrhames/pyrrot-cli)";
    private final String URL = "http://sandbox.thesubdb.com/";

    public ArrayList<String> sendRequest(ArrayList<String> videoHash, String lang) {
        ArrayList<String> subtitle = new ArrayList<>();
        try {
            subtitle = sendGet(videoHash, lang);
        } catch (Exception ex) {
            Logger.getLogger(HTTPRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return subtitle;
    }

    // HTTP GET request
    private ArrayList<String> sendGet(ArrayList<String> videoHashArray, String lang) throws Exception {
        ArrayList<String> subtitleArray = new ArrayList<>();
        for (String videoHash : videoHashArray) {
            String url = URL + "?action=download&hash=" + videoHash + "&language=" + lang;
            System.out.println("subtitlemanager.HTTPRequest.sendGet(): url- " + url);
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

            //add request header
            con.setRequestProperty("User-Agent", USER_AGENT);

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);
            if (responseCode == 200) {
                StringBuilder response;
                try (BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()))) {
                    String inputLine;
                    response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                        response.append("\n");
                    }
                }

                //print result
                //System.out.println("1:"+response.toString());
                String subtitle = response.toString();
                    subtitleArray.add(subtitle);
            }

        }
        /*else if(responseCode == 404){
            JOptionPane.showMessageDialog(null, "Subtitle not found!\n(Response code: "+responseCode+")", "Message", JOptionPane.INFORMATION_MESSAGE);
            return "";
        }else{
            JOptionPane.showMessageDialog(null, "Malformed request!\n(Response code: "+responseCode+")", "Error", JOptionPane.ERROR_MESSAGE);
            return "";
        }*/
        return subtitleArray;
    }

    // HTTP POST request
    private String sendPost() throws Exception {

        String url = "https://selfsolve.apple.com/wcResults.do";
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

}
