/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *
 * @author A5Rhone
 */
public class ParseARP {

    public static void pingBroadcast() {
        for (String s : getIPBroadcast()) {
            try {
                Process pro = Runtime.getRuntime().exec("cmd.exe /c ping " + s);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pro.getInputStream()));

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    line = line.trim().replaceAll(" +", "\t");
                    System.out.println(line);
                }
                //pro.waitFor();      
            } catch (IOException e) {
                System.err.println(e);
                e.printStackTrace();
            }
        }
    }

    public static ArrayList<String> getIPBroadcast() {
        ArrayList<String> broadcasts = new ArrayList<>();
        try {
            Process pro = Runtime.getRuntime().exec("cmd.exe /c arp -a");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pro.getInputStream()));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim().replaceAll(" +", "\t");
                String[] tokens = line.split("\t");
                if (tokens.length == 3 && tokens[1].startsWith("ff-ff-ff-ff-ff-ff")) {
                    broadcasts.add(tokens[0]);
                }
            }
            //pro.waitFor();      
        } catch (IOException e) {
            System.err.println(e);
            e.printStackTrace();
        }

        return broadcasts;
    }

    public static void parse(ArrayList<String> adresses) {
        try {
            Process pro = Runtime.getRuntime().exec("cmd.exe /c arp -a");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pro.getInputStream()));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim().replaceAll(" +", "\t");
                String[] tokens = line.split("\t");
                System.out.println(line);
                if (tokens.length == 3 && tokens[2].startsWith("dynam")) {
                    adresses.add(tokens[0]);

                }
            }
            //pro.waitFor();      
        } catch (IOException e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ArrayList<String> adresses = new ArrayList<>();
        parse(adresses);
        for (String s : adresses) {
            System.out.println(s);
        }
        //pingBroadcast();
    }
}
