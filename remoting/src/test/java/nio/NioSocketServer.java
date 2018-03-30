package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * <pre>
 *
 *  File: NioSocketServer.java
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
public class NioSocketServer {

    private Selector selector;
    private ByteBuffer readBuffer = ByteBuffer.allocate(1024);//调整缓存的大小可以看到打印输出的变化
    private ByteBuffer sendBuffer = ByteBuffer.allocate(1024);//调整缓存的大小可以看到打印输出的变化

    String message = "this is a server";

    public static void main(String[] args) throws IOException {
        System.out.println("server started...");
        new NioSocketServer().start();
    }

    public void start() throws IOException {
        // 打开服务器套接字通道
        ServerSocketChannel channel = ServerSocketChannel.open();
        // 打开服务器套接字通道
        channel.configureBlocking(false);
        // 进行服务的绑定
        channel.bind(new InetSocketAddress("localhost", 8001));
        // 通过open()方法找到Selector
        selector = Selector.open();
        // 注册到selector，等待连接
        channel.register(selector, SelectionKey.OP_ACCEPT);
        while(!Thread.currentThread().isInterrupted()){
            //返回ready的channel
            selector.select();
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while(it.hasNext()){
                SelectionKey key = it.next();
                if (!key.isValid()){
                    continue;
                }
                if ((key.readyOps() & SelectionKey.OP_ACCEPT) != 0){
                    accept(key);
                }else if ((key.readyOps() & SelectionKey.OP_READ) != 0){
                    read(key);
                }else if ((key.readyOps() & SelectionKey.OP_WRITE) != 0){
                    write(key);
                }
            }
        }
    }

    private void write(SelectionKey key) throws IOException, ClosedChannelException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        System.out.println(String.format("server send message:%1$s to client[%2$s]", message, clientChannel.getLocalAddress()));        sendBuffer.clear();
        sendBuffer.put(message.getBytes(Charset.forName("UTF-8")));
        sendBuffer.flip();
        clientChannel.write(sendBuffer);
        clientChannel.register(selector, SelectionKey.OP_READ);
    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();

        // Clear out our read buffer so it's ready for new data
        readBuffer.clear();
        int readNum = 0;
        try {
            readNum = clientChannel.read(readBuffer);
        } catch (IOException e) {
            // The remote forcibly closed the connection, cancel
            // the selection key and close the channel.
            key.cancel();
            clientChannel.close();
        }

        String str = new String(readBuffer.array(), 0, readNum);
        System.out.println(String.format("server receive client[%1$s] message:%2$s", clientChannel.getLocalAddress(), str));
       //client channel register writer
        clientChannel.register(selector, SelectionKey.OP_WRITE);
    }

    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel)key.channel();
        serverSocketChannel.configureBlocking(false);
        //server channel accept client channel
        SocketChannel clientSocketChannel = serverSocketChannel.accept();
        //client channel register read
        clientSocketChannel.register(selector, SelectionKey.OP_READ);
        System.out.println("a new client connected "+clientSocketChannel.getRemoteAddress());
    }

}
