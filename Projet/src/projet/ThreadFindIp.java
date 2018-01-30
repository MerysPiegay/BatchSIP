/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projet;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.attribute.HashAttributeSet;
import static projet.Projet.isPortOpen;

/**
 *
 * @author tecos
 */
public class ThreadFindIp extends Thread {

    private String[] nip;
    private InetAddress addr;
    private int port;
    private int nThread;
    private int timeout;
    private HashMap<InetAddress, Boolean> addresses;

    public ThreadFindIp(String ipS, int pport, int pnThread, HashMap<InetAddress, Boolean> addresses, int timeout) throws IOException {
        nip = ipS.split("[.]");
        port = pport;
        nThread = pnThread;
        this.timeout = timeout;
        this.addresses = addresses;
    }

    @Override
    public void run() {
        byte[] ip = {
            (byte) Integer.parseInt(nip[0]),
            (byte) Integer.parseInt(nip[1]),
            (byte) Integer.parseInt(nip[2]),
            (byte) Integer.parseInt(nip[3])};
        try {
            addr = InetAddress.getByAddress(ip);
        } catch (UnknownHostException ex) {
            Logger.getLogger(ThreadFindIp.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            if (addr.isReachable(timeout)) {
                this.addresses.put(addr, false);
                if (isPortOpen(addr, port)) {
                    this.addresses.put(addr, true);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ThreadFindIp.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String[] getNip() {
        return nip;
    }

    public void setNip(String[] nip) {
        this.nip = nip;
    }

    public InetAddress getAddr() {
        return addr;
    }

    public void setAddr(InetAddress addr) {
        this.addr = addr;
    }

}
