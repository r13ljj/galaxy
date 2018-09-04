package bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
            System.out.println("服务端启动成功");
            Socket socket = serverSocket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // 由系统标准输入设备构造BufferedReader对象
            PrintWriter write = new PrintWriter(System.out);
            String msg = in.readLine();
            while(msg != null && !"end".equals(msg)){
                write.println("server receive:"+msg);
                write.flush();
                msg=in.readLine();
            }
            in.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
