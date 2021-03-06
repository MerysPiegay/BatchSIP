/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projet;

/**
 *
 * @author tecos
 */
import java.net.*;
import java.io.*;
import java.util.*;

/**
 * Find out the local IP address and default gateway
 *
 * @author Henry Zheng
 * @url http://www.ireasoning.com
 */
public class ParseRoute {

    private String _gateway;
    private String _ip;
    private String _subnet_mask;
    private static ParseRoute _instance;

    public static void main(String[] args) {
        try {
            ParseRoute pr = ParseRoute.getInstance();
            System.out.println("Gateway: " + pr.getGateway());
            System.out.println("Subnet Mask: " + pr.getSubnet_mask());
            System.out.println("IP: " + pr.getLocalIPAddress());
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    private ParseRoute() {
        parse();
    }

    private static boolean isWindows() {
        String os = System.getProperty("os.name").toUpperCase();
        return os.contains("WINDOWS");
    }

    private static boolean isLinux() {
        String os = System.getProperty("os.name").toUpperCase();
        return os.contains("LINUX");
    }

    public String getLocalIPAddress() {
        return _ip;
    }

    public String getGateway() {
        return _gateway;
    }

    public String getSubnet_mask() {
        return _subnet_mask;
    }

    public void setSubnet_mask(String _subnet_mask) {
        this._subnet_mask = _subnet_mask;
    }

    public static ParseRoute getInstance() {
        if (_instance == null) {
            _instance = new ParseRoute();
        }
        return _instance;
    }

    private void parse() {
        if (isWindows()) {
            parseWindows();
        } else if (isLinux()) {
            parseLinux();
        }
    }

    private void parseWindows() {
        try {
            Process pro = Runtime.getRuntime().exec("cmd.exe /c route -4 print");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pro.getInputStream()));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim().replaceAll(" +", "\t");
                String[] tokens = line.split("\t");
                if (tokens.length == 5 && tokens[0].equals("0.0.0.0")) {
                    System.out.println(line);
                    _gateway = tokens[2];
                    _subnet_mask = tokens[3];
                    _ip = tokens[3];
                    return;
                }
            }
            //pro.waitFor();      
        } catch (IOException e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }

    private void parseLinux() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("/proc/net/route"));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                String[] tokens = line.split("\t");
                if (tokens.length > 1 && tokens[1].equals("00000000")) {
                    String gateway = tokens[2]; //0102A8C0
                    if (gateway.length() == 8) {
                        String[] s4 = new String[4];
                        s4[3] = String.valueOf(Integer.parseInt(gateway.substring(0, 2), 16));
                        s4[2] = String.valueOf(Integer.parseInt(gateway.substring(2, 4), 16));
                        s4[1] = String.valueOf(Integer.parseInt(gateway.substring(4, 6), 16));
                        s4[0] = String.valueOf(Integer.parseInt(gateway.substring(6, 8), 16));
                        _gateway = s4[0] + "." + s4[1] + "." + s4[2] + "." + s4[3];
                    }
                    String subnet_mask = tokens[7]; //0102A8C0
                    if (subnet_mask.length() == 8) {
                        String[] s4 = new String[4];
                        s4[3] = String.valueOf(Integer.parseInt(subnet_mask.substring(0, 2), 16));
                        s4[2] = String.valueOf(Integer.parseInt(subnet_mask.substring(2, 4), 16));
                        s4[1] = String.valueOf(Integer.parseInt(subnet_mask.substring(4, 6), 16));
                        s4[0] = String.valueOf(Integer.parseInt(subnet_mask.substring(6, 8), 16));
                        _subnet_mask = s4[0] + "." + s4[1] + "." + s4[2] + "." + s4[3];
                    }
                    String iface = tokens[0];
                    NetworkInterface nif = NetworkInterface.getByName(iface);
                    Enumeration addrs = nif.getInetAddresses();
                    while (addrs.hasMoreElements()) {
                        Object obj = addrs.nextElement();
                        if (obj instanceof Inet4Address) {
                            _ip = obj.toString();
                            if (_ip.startsWith("/")) {
                                _ip = _ip.substring(1);
                            }
                            return;
                        }
                    }
                    return;
                }
            }
            reader.close();
        } catch (IOException e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }

}
