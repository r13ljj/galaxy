package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * <pre>
 *
 *  File: UDPEchoServerTest.java
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
public class UDPEchoServerTest {

    private static final int ECHOMAX = 255;

    public static void main(String[] args) throws IOException {

        DatagramSocket socket = new DatagramSocket(7);
        DatagramPacket packet = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);

        while (true) {
            socket.receive(packet);
            System.out.println("Handling client at " + packet.getAddress()+", server receive:"+new String(packet.getData()));

            socket.send(packet);
            packet.setLength(ECHOMAX);
        }

    }

}
