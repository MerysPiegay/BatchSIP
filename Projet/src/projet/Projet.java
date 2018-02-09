/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projet;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;
import jpcap.JpcapCaptor;

/**
 *
 * @author tecos
 */
public class Projet {

    static final int MAX_ACTIVE = 15000;
    static final int PORT = 16400;
    static final int TIMEOUT = 10000;

    static boolean isPortOpen(InetAddress addr, int port) {
        try {
            Socket socket;
            socket = new Socket();
            socket.connect(new InetSocketAddress(addr, port));
            socket.close();
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    static void definePasserelle() throws UnknownHostException, SocketException {
        Enumeration<NetworkInterface> ei = NetworkInterface.getNetworkInterfaces();
        while (ei.hasMoreElements()) {
            NetworkInterface inter;
            inter = ei.nextElement();
            Enumeration<InetAddress> inetAddress = inter.getInetAddresses();
            InetAddress currentAddress;
            currentAddress = inetAddress.nextElement();
            while (inetAddress.hasMoreElements()) {
                currentAddress = inetAddress.nextElement();
                if (currentAddress instanceof Inet4Address && !currentAddress.isLoopbackAddress()) {
                    System.out.println(currentAddress.toString());
                    break;
                }
            }

        }
    }

    /**
     * @param args the command line arguments
     * @throws java.net.UnknownHostException
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException, Exception {
        HashMap<InetAddress, Boolean> addresses;
        addresses = new HashMap<>();
        Set<Thread> baseThreads = Thread.getAllStackTraces().keySet();

        /*for (int i = 0; i < 256; i += 1) {
         for (int j = 0; j < 256; j += 1) {
         System.out.println((Projet.passerelle + (i) + "." + (j)));
         while (Thread.activeCount() > MAX_ACTIVE);
         new ThreadFindIp(Projet.passerelle + (i) + "." + (j), PORT, addresses, TIMEOUT).start();

         }
         }
         */
        ArrayList<String> adresses = new ArrayList<>();

        jpcap.NetworkInterface[] devices = JpcapCaptor.getDeviceList();
        Scanner sc = new Scanner(System.in);
        int n = 0;
        for (jpcap.NetworkInterface d : devices) {
            n++;
            System.out.print(n + ") ");
            System.out.println(d.addresses[0].address);
            System.out.println(d.addresses[1].address);
        }
        int i = sc.nextInt();
        ARP.arp(devices[i - 1]);

       // ParseARP.parse(adresses);
        /*
         for (String adresse : adresses) {
         System.out.println(adresse);
         new ThreadFindIp(adresse, PORT, addresses, TIMEOUT).start();
         }
         Set<Thread> threads = Thread.getAllStackTraces().keySet();
         threads.removeAll(baseThreads);
         Thread[] threadArray = threads.toArray(new Thread[threads.size()]);
         for (Thread threadArray1 : threadArray) {
         threadArray1.join();
         System.out.println(addresses);
         }
         */
    }

}
