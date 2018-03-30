package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Scanner;

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


    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8001;

    private ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
    private ByteBuffer readBuffer = ByteBuffer.allocate(1024);

    private Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws Exception{
        NioSocketClient client = new NioSocketClient();
        client.start();
    }

    public void start()throws IOException{
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        //client is connect(server address)
        socketChannel.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT));
        Selector selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
        while(!Thread.currentThread().isInterrupted()){
            //选择一组键，其相应的通道已为 I/O 操作准备就绪。
            //此方法执行处于阻塞模式的选择操作。
            selector.select();
            //返回此选择器的已选择selectionKey。
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while(it.hasNext()){
                SelectionKey key = it.next();
                it.remove();
                if (!key.isValid()){
                    continue;
                }
                try {
                    if ((key.readyOps() & SelectionKey.OP_CONNECT) != 0) {
                        socketChannel.finishConnect();
                        socketChannel.register(selector, SelectionKey.OP_WRITE);
                        System.out.println(String.format("client has connected server[%1$s:%2$d]", SERVER_HOST, SERVER_PORT));
                        break;
                    }else if ((key.readyOps() & SelectionKey.OP_WRITE) != 0){
                        System.out.println("please input messasge:");
                        String input = scanner.nextLine();
                        writeBuffer.clear();
                        writeBuffer.put(input.getBytes(Charset.forName("UTF-8")));
                        //将缓冲区各标志复位,因为向里面put了数据标志被改变要想从中读取数据发向服务器,就要复位
                        writeBuffer.flip();
                        socketChannel.write(writeBuffer);
                        //注册写操作,每个chanel只能注册一个操作，最后注册的一个生效
                        //如果你对不止一种事件感兴趣，那么可以用“位或”操作符将常量连接起来
                        //int interestSet = SelectionKey.OP_READ | SelectionKey.OP_WRITE;
                        //使用interest集合
                        socketChannel.register(selector, SelectionKey.OP_READ);
                        //socketChannel.register(selector, SelectionKey.OP_WRITE);
                        //socketChannel.register(selector, SelectionKey.OP_READ);
                        //socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                    }else if ((key.readyOps() & SelectionKey.OP_READ) != 0){
                        SocketChannel channel = (SocketChannel)key.channel();
                        //清空缓存区
                        readBuffer.clear();
                        int readNum = channel.read(readBuffer);
                        String str = new String(readBuffer.array(), 0, readNum);
                        System.out.println(String.format("client receive server[%1$s] message:%2$s", socketChannel.getRemoteAddress(), str));
                        //注册读操作，下一次读取
                        socketChannel.register(selector, SelectionKey.OP_WRITE);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
