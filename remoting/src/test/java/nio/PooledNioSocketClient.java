package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.*;

/**
 * <pre>
 *
 *  File: PooledNioSocketClient.java
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
public class PooledNioSocketClient {

    private static ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();

    private Queue<SocketChannel>  pool;
    private ChannelHandler channelHandler;

    private String _server_host;
    private int _server_port;
    private int _pool_size;

    Selector selector;

    ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
    ByteBuffer readBuffer = ByteBuffer.allocate(1024);

    public PooledNioSocketClient(String serverHost, int serverPort, int poolSize) {
        this._server_host = serverHost;
        this._server_port = serverPort;
        this._pool_size = poolSize;
        pool = new ConcurrentLinkedDeque<>();
        channelHandler = new ChannelHandler();
        init();
    }

    public SocketChannel getConnection(){
        int poolSize = pool.size();
        if (poolSize == 0){
            return null;
        }
        int n = threadLocalRandom.nextInt(poolSize-1);
        return pool.poll();
    }

    public void returnConnection(SocketChannel channel){
        if (channel == null)
            return;
        pool.add(channel);
    }

    private void init(){
        for(int i=0; i<_pool_size; i++){
            try {
                SocketChannel connection = this.createChannel();
                channelHandler.addChannel(connection);
                pool.add(connection);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private SocketChannel createChannel() throws IOException{
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        //client is connect(server address)
        socketChannel.connect(new InetSocketAddress(_server_host, _server_port));
        System.out.println("create connection ssuccess.");
        return socketChannel;
    }

    class ChannelHandler {

        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(10, 10,
                3000, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1000));

        public ChannelHandler() {
            try {
                selector = Selector.open();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void addChannel(SocketChannel channel){
            poolExecutor.execute(new MessageHandler(channel));
        }


    }

    class MessageHandler implements Runnable{

        SocketChannel socketChannel;
        Scanner scanner;

        public MessageHandler(SocketChannel channel) {
            this.socketChannel = channel;
            initial();
        }

        private void initial(){
            try {
                socketChannel.register(selector, SelectionKey.OP_CONNECT);
                System.out.println(String.format("channel:%1$s registed", socketChannel.getLocalAddress()));
                scanner = new Scanner(System.in);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted()){
                //选择一组键，其相应的通道已为 I/O 操作准备就绪。
                //此方法执行处于阻塞模式的选择操作。
                try {
                    selector.select();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //返回此选择器的已选择selectionKey。
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                while(it.hasNext()){
                    SelectionKey key = it.next();
                    it.remove();
                    if (!key.isValid()){
                        continue;
                    }
                    try {
                        //SocketChannel socketChannel = (SocketChannel)key.channel();
                        String serverAddress = socketChannel.getRemoteAddress().toString();
                        if ((key.readyOps() & SelectionKey.OP_CONNECT) != 0) {
                            socketChannel.finishConnect();
                            socketChannel.register(selector, SelectionKey.OP_WRITE);
                            System.out.println(String.format("client has connected server[%1$s]", serverAddress));
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

}
