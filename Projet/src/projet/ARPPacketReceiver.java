/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projet;

import java.net.InetAddress;
import java.util.ArrayList;
import jpcap.PacketReceiver;
import jpcap.packet.ARPPacket;
import jpcap.packet.Packet;

/**
 *
 * @author A5Rhone
 */
class ARPPacketReceiver implements PacketReceiver {

    ArrayList<InetAddress> adresses;
    ArrayList<String> macs;
    InetAddress local;

    public ARPPacketReceiver(InetAddress local) {
        this.adresses = new ArrayList<>();
        this.macs = new ArrayList<>();
        this.local = local;
    }

    @Override
    public void receivePacket(Packet packet) {
//((ARPPacket) packet).target_protoaddr)
        if (((ARPPacket) packet).getTargetProtocolAddress().equals(local)) {
            this.adresses.add((InetAddress) ((ARPPacket) packet).getSenderProtocolAddress());
            this.macs.add(((ARPPacket) packet).getSenderHardwareAddress().toString());
        }
    }

}
