package collect.thrift;

import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.api.*;
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
public class MyApp {

    public static void main(String[] args) {

        MySecureRpcClientFacade client = new MySecureRpcClientFacade();

        //init properties
        Properties props = new Properties();
        props.setProperty(RpcClientConfigurationConstants.CONFIG_CLIENT_TYPE, "collect/avro/thrift");
        props.setProperty("hosts", "h1");
        props.setProperty("hosts.h1", "client.example.org"+":"+ String.valueOf(41414));
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

}

class MySecureRpcClientFacade {

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
