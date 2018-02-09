/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projet;

import jpcap.JpcapCaptor;

/**
 *
 * @author A5Rhone
 */
class CaptureThread extends Thread {

    JpcapCaptor captor;
    ARPPacketReceiver aRPPacketReceiver;

    public CaptureThread() {
    }

    CaptureThread(JpcapCaptor captor, ARPPacketReceiver aRPPacketReceiver) {
        this.aRPPacketReceiver = aRPPacketReceiver;
        this.captor = captor;
    }

    @Override
    public void run() {
        captor.loopPacket(-1, aRPPacketReceiver);
    }
}
