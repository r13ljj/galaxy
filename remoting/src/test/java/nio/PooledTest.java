package nio;

import java.nio.channels.SocketChannel;

/**
 * <pre>
 *
 *  File: PooledTest.java
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
public class PooledTest {

    public static void main(String[] args) {
        try {
            PooledNioSocketClient poolClient = new PooledNioSocketClient("localhost", 8001, 10);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

    }

}
