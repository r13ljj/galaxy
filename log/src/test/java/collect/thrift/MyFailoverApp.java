package collect.thrift;

import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.api.RpcClient;
import org.apache.flume.api.SecureRpcClientFactory;
import org.apache.flume.event.EventBuilder;

import java.nio.charset.Charset;
import java.util.Properties;

/**
 * <pre>
 *
 *  File: collect.thrift.MyApp.java
 *
 *  Copyright (c) 2018, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,					Who,					What;
 *  2018/3/29				lijunjun				Initial.
 *
 * </pre>
 */
public class MyFailoverApp {

    public static void main(String[] args) {

        MySecureRpcClientFacade client = new MySecureRpcClientFacade();

        //init properties
        Properties props = failoverProperties();

        // Initialize client with the kerberos authentication related properties
        props.setProperty("kerberos", "true");
        props.setProperty("client-principal", "flumeclient/client.example.org@EXAMPLE.ORG");
        props.setProperty("client-keytab", "/tmp/flumeclient.keytab");
        props.setProperty("server-principal", "flume/server.example.org@EXAMPLE.ORG");
        client.init(props);

        //send data
        String sampleData = "Hello Flume!";
        for (int i = 0; i < 10; i++) {
            client.sendDataToFlume(sampleData+i);
        }
        //close
        client.cleanUp();

    }

    private static Properties failoverProperties(){
        Properties props = new Properties();
        //failover
        props.put("client.type", "default_failover");
        // List of hosts (space-separated list of user-chosen host aliases)
        props.put("hosts", "h1 h2 h3");
        // host/port pair for each host alias
        String host1 = "host1.example.org:41414";
        String host2 = "host2.example.org:41414";
        String host3 = "host3.example.org:41414";
        props.put("hosts.h1", host1);
        props.put("hosts.h2", host2);
        props.put("hosts.h3", host3);
        return props;
    }

    public static Properties loadBalanceProperties(){
        Properties props = new Properties();
        props.put("client.type", "default_loadbalance");

        // List of hosts (space-separated list of user-chosen host aliases)
        props.put("hosts", "h1 h2 h3");

        // host/port pair for each host alias
        String host1 = "host1.example.org:41414";
        String host2 = "host2.example.org:41414";
        String host3 = "host3.example.org:41414";
        props.put("hosts.h1", host1);
        props.put("hosts.h2", host2);
        props.put("hosts.h3", host3);

        props.put("host-selector", "random"); // For random host selection
        // props.put("host-selector", "round_robin"); // For round-robin host
        //                                            // selection
        props.put("backoff", "true"); // Disabled by default.

        props.put("maxBackoff", "10000"); // Defaults 0, which effectively
        return props;
    }

}

class MyFailoverRpcClientFacade {

    private RpcClient client;
    private Properties properties;

    public void init(Properties props){
        this.properties = props;
        //this.client = RpcClientFactory.getDefaultInstance(host, port);
        this.client = SecureRpcClientFactory.getThriftInstance(properties);
    }

    public void sendDataToFlume(String data){
        Event event = EventBuilder.withBody(data, Charset.forName("UTF-8"));
        try {
            client.append(event);
        } catch (EventDeliveryException e) {
            // clean up and recreate the client
            client.close();
            client = null;
            client = SecureRpcClientFactory.getThriftInstance(properties);
        }
    }

    public void cleanUp(){
        client.close();
    }

}
