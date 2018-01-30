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
import java.util.Set;

/**
 *
 * @author tecos
 */
public class Projet {

    static final int N_THREAD = 32;
    static final int PORT = 80;
    static final int TIMEOUT = 10000;
    static public String passerelle = "192.168.0.";

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
    public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
        HashMap<InetAddress, Boolean> addresses;
        addresses = new HashMap<>();
        definePasserelle();
        Set<Thread> baseThreads = Thread.getAllStackTraces().keySet();

        for (int i = 0; i < 256; i += N_THREAD) {
            for (int j = 0; j < N_THREAD; j++) {
                new ThreadFindIp(Projet.passerelle + (i + j), PORT, N_THREAD, addresses, TIMEOUT).start();
            }
        }
        Set<Thread> threads = Thread.getAllStackTraces().keySet();
        threads.removeAll(baseThreads);
        Thread[] threadArray = threads.toArray(new Thread[threads.size()]);
        for (Thread threadArray1 : threadArray) {
            threadArray1.join();
        }
        System.out.println(addresses);
    }

}
