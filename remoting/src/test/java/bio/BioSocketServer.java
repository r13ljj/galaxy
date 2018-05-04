package bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * <pre>
 *
 *  File: BioSocketServer.java
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
public class BioSocketServer {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(9999);
            System.out.println("客户端启动成功");
            Socket socket = serverSocket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
