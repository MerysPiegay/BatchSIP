/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;

/**
 *
 * @author A5Rhone
 */
public class URLGet {

    public static String getHTML(String urlToRead) throws Exception {
        Authenticator.setDefault(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("admin", "commend".toCharArray());
            }
        });
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlToRead);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        conn.disconnect();
        return result.toString();
    }

    public static String disp(String html) {
        StringBuilder sb = new StringBuilder();
        StringBuilder tabd = new StringBuilder();
        html = html.trim().replaceAll("<", "\n<");
        for (String s : html.split("\n")) {
            if (s.startsWith("<!--")) {
                sb.append("\t");
                sb.append(tabd);
            } else {
                if (s.startsWith("</")) {
                    tabd.deleteCharAt(tabd.length() - 1);
                    sb.append(tabd);
                    sb.append("\t");
                } else {
                    if (s.startsWith("<")) {
                        tabd.append("\t");
                        sb.append(tabd);
                    }
                }
            }
            sb.append(s);
            sb.append('\n');
        }
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        Authenticator.setDefault(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("admin", "commend".toCharArray());
            }
        });

        //System.out.println(disp(getHTML("http://192.168.1.62/cgi-bin/home.cgi")));
        URL url = new URL("http://192.168.1.62/cgi-bin/home.cgi");

        BufferedReader bin = new BufferedReader(
                new InputStreamReader(url.openStream()));

        String line;
        while ((line = bin.readLine()) != null) {
            System.out.println(line);
        }
    }
}
