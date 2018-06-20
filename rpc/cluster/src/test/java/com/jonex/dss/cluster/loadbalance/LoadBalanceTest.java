package com.jonex.dss.cluster.loadbalance;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * 负载均衡算法:
 * 1.Random随机算法
 * 2.Weight Random加权随机
 * 3.Round Robbin轮询
 * 4.Weighted Round Robbin 加权轮询
 * 5.Least Connections最小连接
 * 6.Weighted Least Connections最小连接
 * 7.Common Hash普通哈希
 * 8.Consistent Hash一致性哈希
 * 9.Ip Hash ip地址散列
 * 10.Url Hash url地址散列
 *
 */
public class LoadBalanceTest {

    private static final int VIRTUAL_NODE_NUM = 5;

    private Integer roundIndex = 0;

    private List<ServerConnection> serverConnections = new LinkedList<ServerConnection>(){
        {
            for (Iterator<String> it = serverMap.keySet().iterator();it.hasNext();){
                add(new ServerConnection(it.next(), new Integer(0)));
            }
        }
    };

    public String random(){
        List<String> serverList = new ArrayList<>(serverMap.keySet());
        Random random = new Random();
        String hitServer = serverList.get(random.nextInt(serverList.size()));
        System.out.println("random hit server:"+hitServer);
        return hitServer;
    }

    public String weightRandom(){
        Set<String> keySet = serverMap.keySet();
        List<String> servers = new ArrayList<>();
        for (Iterator<String> it = keySet.iterator();it.hasNext();){
            String serverIp = it.next();
            int weight = serverMap.get(serverIp);
            for (int i=0; i<weight; i++){
                servers.add(serverIp);
            }
        }
        Random random = new Random();
        String server = servers.get(random.nextInt(servers.size()));
        System.out.println("weight random hit server:"+server);
        return server;
    }

    public String roundRobbin(){
        String server = "";
        List<String> serverList = new ArrayList<>(serverMap.keySet());
        synchronized (roundIndex){
            if (roundIndex >= serverList.size()){
                roundIndex = 0;
            }
            server = serverList.get(roundIndex);
            roundIndex++;
        }
        System.out.println("round robbin hit server:"+server);
        return server;
    }

    public String weightRoundRobbin(){
        String server = "";
        Set<String> keySet = serverMap.keySet();
        List<String> servers = new ArrayList<>();
        for (Iterator<String> it = keySet.iterator();it.hasNext();){
            String serverIp = it.next();
            int weight = serverMap.get(serverIp);
            for (int i=0; i<weight; i++){
                servers.add(serverIp);
            }
        }
        synchronized (roundIndex){
            if (roundIndex >= servers.size()){
                roundIndex = 0;
            }
            server = servers.get(roundIndex);
            roundIndex++;
        }
        System.out.println("weight round robbin hit server:"+server);
        return server;
    }

    /**
     * 在多个服务器中，与处理连接数(会话数)最少的服务器进行通信的算法。
     * 即使在每台服务器处理能力各不相同，每笔业务处理量也不相同的情况下，也能够在一定程度上降低服务器的负载。
     *
     */
    public String leastConnections(){
        String server = "";
        Collections.sort(serverConnections, new Comparator<ServerConnection>() {
            @Override
            public int compare(ServerConnection o1, ServerConnection o2) {
                if (o1 != null && o2 != null && o1.getConnections() > o2.getConnections()){
                    return -1;
                }else if(o1 != null && o2 != null && o1.getConnections() < o2.getConnections()){
                    return 1;
                }
                return 0;
            }
        });
        int connections = serverConnections.get(0).getConnections();
        server = serverConnections.get(0).getServer();
        System.out.println("least connections:"+connections+" hit server:"+server);
        return server;
    }

    /**
     * 为最少连接算法中的每台服务器附加权重的算法。
     * 该算法事先为每台服务器分配处理连接的数量，并将客户端请求转至连接数最少的服务器上。
     *
     */
    public String weightLeastConnections(){
        //类似其他加权算法？
        return "";
    }

    public String commonHash(){
        String server = "";
        List<String> serverList = new ArrayList<>(serverMap.keySet());
        String clientIp = "192.168.1.155";
        int idx = clientIp.hashCode() % serverList.size();
        server = serverList.get(idx);
        System.out.println("common hash hit server:"+server);
        return server;
    }

    /**
     * 1.CRC32,MD5,SHA-1 加密HASH算法
     * 2.MurmurHash 非加密HASH算法
     *
     */
    public String consistentHash(){
        String requestId = "xxx";
        List<String> serverList = new ArrayList<>(serverMap.keySet());
        //首先是将node定位到圆上,我们以 hash - address方式定位
        //因为后面需要获取离requestId最近node所以将数据放入到TreeMap中
        TreeMap<Long, String> serverRing = new TreeMap<Long, String>();
        serverList.forEach(server -> {
            for (int i=0; i<VIRTUAL_NODE_NUM; i++){
                long serverHash = hash("SHARD-" + server + "-NODE-" + i);
                serverRing.put(serverHash, server);
            }
        });
        long requestHash = hash(requestId);
        //这里是顺时针转动requestHash寻找node的策略,其实就是寻找node哈希值大于等于requestId哈希值的最近一个node
        SortedMap<Long, String> lastRing = serverRing.tailMap(requestHash);
        if (!lastRing.isEmpty()){
            return lastRing.get(lastRing.firstKey());
        }
        //如果请求落在最大一组hash上,那么就返回第一个node
        return serverRing.firstEntry().getValue();
    }
    private long hash(String key){
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        md5.reset();
        byte[] bytes = null;
        try {
            bytes = key.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        md5.update(bytes);
        byte[] digest = md5.digest();
        long hashCode = (((long) (digest[3] & 0xFF) << 24)
                | ((long) (digest[2] & 0xFF) << 16)
                | ((long) (digest[1] & 0xFF) << 8)
                | (digest[0] & 0xFF));
        long truncateHashCode = hashCode & 0xffffffffL;
        return truncateHashCode;
    }
    /**
     *  MurMurHash算法，是非加密HASH算法，性能很高，
     *  比传统的CRC32,MD5，SHA-1（这两个算法都是加密HASH算法，复杂度本身就很高，带来的性能上的损害也不可避免）
     *  等HASH算法要快很多，而且据说这个算法的碰撞率很低.
     *  http://murmurhash.googlepages.com/
     */
    private Long murmurhash(String key) {

        ByteBuffer buf = ByteBuffer.wrap(key.getBytes());
        int seed = 0x1234ABCD;

        ByteOrder byteOrder = buf.order();
        buf.order(ByteOrder.LITTLE_ENDIAN);

        long m = 0xc6a4a7935bd1e995L;
        int r = 47;

        long h = seed ^ (buf.remaining() * m);

        long k;
        while (buf.remaining() >= 8) {
            k = buf.getLong();

            k *= m;
            k ^= k >>> r;
            k *= m;

            h ^= k;
            h *= m;
        }

        if (buf.remaining() > 0) {
            ByteBuffer finish = ByteBuffer.allocate(8).order(
                    ByteOrder.LITTLE_ENDIAN);
            // for big-endian version, do this first:
            // finish.position(8-buf.remaining());
            finish.put(buf).rewind();
            h ^= finish.getLong();
            h *= m;
        }

        h ^= h >>> r;
        h *= m;
        h ^= h >>> r;

        buf.order(byteOrder);
        return h;
    }

    /**
     * 通过管理发送方IP和目的地IP地址的散列，将来自同一发送方的分组(或发送至同一目的地的分组)统一转发到相同服务器。
     * 当客户端有一系列业务需要处理而必须和一个服务器反复通信时，该算法能够以流(会话)为单位，保证来自相同客户端的通信能够一直在同一服务器中进行处理。
     *
     */
    public String ipHash(){
        String requestIp = "";
        return "";
    }

    /**
     * 通过管理客户端请求URL信息的散列，将发送至相同URL的请求转发至同一服务器
     *
     */
    public String urlHash(){
        String url = "";
        return "";
    }



    /**
     * 服务连接状态
     */
    class ServerConnection{
        private String server;
        private Integer connections;

        public ServerConnection(String server, Integer connections) {
            this.server = server;
            this.connections = connections;
        }

        public String getServer() {
            return server;
        }

        public void setServer(String server) {
            this.server = server;
        }

        public Integer getConnections() {
            return connections;
        }

        public void setConnections(Integer connections) {
            this.connections = connections;
        }
    }




    private Map<String,Integer> serverMap = new HashMap<String,Integer>(){{
        put("192.168.1.100",1);
        put("192.168.1.101",1);
        put("192.168.1.102",4);
        put("192.168.1.103",1);
        put("192.168.1.104",1);
        put("192.168.1.105",3);
        put("192.168.1.106",1);
        put("192.168.1.107",2);
        put("192.168.1.108",1);
        put("192.168.1.109",1);
        put("192.168.1.110",1);
    }};

}
