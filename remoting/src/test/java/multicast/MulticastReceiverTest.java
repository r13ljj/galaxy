package multicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * <pre>
 *
 *  File: MulticastReceiverTest.java
 *
 *  Copyright (c) 2018, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,					Who,					What;
 *  2018/4/27				lijunjun				Initial.
 *
 * </pre>
 */
public class MulticastReceiverTest {

    public static void main(String[] args) throws Exception {

        final InetAddress address = InetAddress.getByName("224.1.1.1");
        final int port = 45599;

        for (int i = 0; i < 5; i++) {
            new Thread("Thread #" + i){
                @Override
                public void run() {
                    try {
                        MulticastSocket sock = new MulticastSocket(port);
                        sock.joinGroup(address);

                        byte[] msg = new byte[256];
                        DatagramPacket packet = new DatagramPacket(msg, msg.length);

                        sock.receive(packet);
                        System.out.println(Thread.currentThread().getName() +
                                " receive: " + new String(packet.getData()));
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }

        Thread.sleep(2000);

        MulticastSocket sock = new MulticastSocket();
        sock.setTimeToLive(32);

        byte[] msg = "hellomulticast".getBytes();
        DatagramPacket packet = new DatagramPacket(msg, msg.length, address, port);

        sock.send(packet);
        System.out.println("Message sent");
    }

}
