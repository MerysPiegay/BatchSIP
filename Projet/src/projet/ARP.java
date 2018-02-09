/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projet;

/**
 *
 * @author A5Rhone
 */
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import jpcap.*;
import jpcap.packet.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import static projet.URLGet.getHTML;

public class ARP {

    public static void arp(NetworkInterface device) throws java.io.IOException, InterruptedException, Exception {
        //find network interface

        //open Jpcap
        JpcapCaptor captor = JpcapCaptor.openDevice(device, 2000, false, 3000);

        captor.setFilter("arp", true);
        JpcapSender sender = captor.getJpcapSenderInstance();

        InetAddress srcip = null;
        for (NetworkInterfaceAddress addr : device.addresses) {
            if (addr.address instanceof Inet4Address) {
                srcip = addr.address;
                break;
            }
        }

        byte[] broadcast = new byte[]{(byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255};
        ARPPacket arp = new ARPPacket();
        arp.hardtype = ARPPacket.HARDTYPE_ETHER;
        arp.prototype = ARPPacket.PROTOTYPE_IP;
        arp.operation = ARPPacket.ARP_REQUEST;
        arp.hlen = 6;
        arp.plen = 4;
        arp.sender_hardaddr = device.mac_address;
//        arp.sender_protoaddr = ip.getAddress();
        arp.sender_protoaddr = device.addresses[1].address.getAddress();
        arp.target_hardaddr = new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0};
        arp.target_protoaddr = broadcast;

        EthernetPacket ether = new EthernetPacket();
        ether.frametype = EthernetPacket.ETHERTYPE_ARP;
        ether.src_mac = device.mac_address;
        ether.dst_mac = broadcast;
        arp.datalink = ether;

        sender.sendPacket(arp);
        ARPPacket p = null;
        while (p == null) {
            p = (ARPPacket) captor.getPacket();
            System.out.println(p);

        }
        ARPPacketReceiver aRPPacketReceiver = new ARPPacketReceiver(device.addresses[1].address);
        Thread t = new CaptureThread(captor, aRPPacketReceiver);
        t.start();
        Thread.sleep(10000);
        captor.breakLoop();
        for (int i = 0; i < aRPPacketReceiver.adresses.size(); i++) {
            if (aRPPacketReceiver.macs.get(i).startsWith("00:0c:ab")) {
                try {
                    Document doc;
                    doc = Jsoup.parse(getHTML("http:/" + aRPPacketReceiver.adresses.get(i) + "/cgi-bin/network.cgi"));
                    Element body = doc.select("body").first();

                    for (Element e : body.getElementsByTag("tr")) {
                        if (e.className().startsWith("content")) {
                            //System.out.println(e);
                            System.out.print(e.getElementsByClass("contentkey").text());

                            // TODO
                        }
                    }

                } catch (Exception ex) {
                    Logger.getLogger(ARP.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    public static void main(String[] args) throws Exception {
        NetworkInterface[] devices = JpcapCaptor.getDeviceList();
        Scanner sc = new Scanner(System.in);
        int n = 0;
        for (NetworkInterface d : devices) {
            n++;
            System.out.print(n + ") ");
            System.out.println(d.addresses[0].address);
            System.out.println(d.addresses[1].address);
        }
        int i = sc.nextInt();
        ARP.arp(devices[i - 1]);

    }
}
