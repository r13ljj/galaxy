package nio;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * <pre>
 *
 *  File: NioSocketClient.java
 *
 *  Copyright (c) 2018, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,					Who,					What;
 *  2018/3/30				lijunjun				Initial.
 *
 * </pre>
 */
public class NioSocketClient {

    private ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
    private ByteBuffer readBuffer = ByteBuffer.allocate(1024);

    public static void main(String[] args) throws Exception{
        NioSocketClient client = new NioSocketClient();
        client.start();
    }

    public void start()throws IOException{
        
    }

}
