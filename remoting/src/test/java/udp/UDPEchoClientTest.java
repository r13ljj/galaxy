package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * <pre>
 *
 *  File: UDPEchoClientTest.java
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
public class UDPEchoClientTest {

    public static void main(String[] args) throws IOException {

        InetAddress serverAddress = InetAddress.getByName(args[0]);
        byte[] bytesToSend = args[1].getBytes();

        DatagramSocket socket = new DatagramSocket();
        socket.setSoTimeout(3000);

        DatagramPacket sendPacket = new DatagramPacket(
                bytesToSend, bytesToSend.length, serverAddress, 7);

        DatagramPacket receivePacket = new DatagramPacket(
                new byte[bytesToSend.length], bytesToSend.length);

        // Packets may be lost, so we have to keep trying
        int tries = 0;
        boolean receivedResponse = false;
        do {
            socket.send(sendPacket);
            try {
                socket.receive(receivePacket);
                if (!receivePacket.getAddress().equals(serverAddress))
                    throw new IOException("Receive from unknown source");
                receivedResponse = true;
            }
            catch (IOException e) {
                tries++;
                System.out.println("Timeout, try again");
            }
        } while (!receivedResponse && tries < 5);

        if (receivedResponse)
            System.out.println("client Received: " + new String(receivePacket.getData()));
        else
            System.out.println("No response");

        socket.close();
    }

}
