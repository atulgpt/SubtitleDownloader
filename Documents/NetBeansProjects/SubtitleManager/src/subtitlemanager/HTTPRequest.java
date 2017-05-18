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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.JOptionPane;

/**
 *
 * @author atulgupta
 */
public class HTTPRequest {

    private final String USER_AGENT = "SubDB/1.0 (Pyrrot/0.1; http://github.com/jrhames/pyrrot-cli)";
    private final String URL = "http://sandbox.thesubdb.com/";

    public String sendRequest(String videoHash, String lang) {
        String subtitle = "";
        try {
            subtitle = sendGet(videoHash, lang);
        } catch (Exception ex) {
            Logger.getLogger(HTTPRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return subtitle;
    }

    // HTTP GET request
    private String sendGet(String videoHash, String lang) throws Exception {

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
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
                response.append("\n");
            }
            in.close();

            //print result
            //System.out.println("1:"+response.toString());
            return response.toString();
        }
        else if(responseCode == 404){
            JOptionPane.showMessageDialog(null, "Subtitle not found!\n(Response code: "+responseCode+")", "Message", JOptionPane.INFORMATION_MESSAGE);
            return "";
        }else{
            JOptionPane.showMessageDialog(null, "Malformed request!\n(Response code: "+responseCode+")", "Error", JOptionPane.ERROR_MESSAGE);
            return "";
        }
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
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        //print result
        //System.out.println(response.toString());
        return response.toString();
    }

}
