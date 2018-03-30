package collect.avro;

import org.apache.flume.Channel;
import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.Transaction;
import org.apache.flume.agent.embedded.EmbeddedAgent;
import org.apache.flume.channel.MemoryChannel;
import org.apache.flume.event.EventBuilder;

import java.nio.charset.Charset;
import java.util.*;

/**
 * <pre>
 *
 *  File: MyEmbeddedApp.java
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
public class MyEmbeddedApp {

    public static void main(String[] args) {

        MyEmbeddedClientFacade client = new MyEmbeddedClientFacade();

        Map<String, String> properties = new HashMap<String, String>();
        properties.put("channel.type", "memory");
        properties.put("channel.capacity", "200");
        properties.put("sinks", "sink1 sink2");
        properties.put("sink1.type", "collect/avro");
        properties.put("sink2.type", "collect/avro");
        properties.put("sink1.hostname", "collector1.apache.org");
        properties.put("sink1.port", "5564");
        properties.put("sink2.hostname", "collector2.apache.org");
        properties.put("sink2.port",  "5565");
        properties.put("processor.type", "load_balance");
        properties.put("source.interceptors", "i1");
        properties.put("source.interceptors.i1.type", "static");
        properties.put("source.interceptors.i1.key", "key1");
        properties.put("source.interceptors.i1.value", "value1");

        client.init(properties);

        List<String> datas = new ArrayList<>();
        for(int i=0; i<10; i++){
            datas.add("hello flume"+i);
        }
        client.sendDataToFlume(datas);

        client.cleanUp();
    }

}

class MyEmbeddedClientFacade{
    private EmbeddedAgent agent;
    private Map<String, String> properties;

    public void init(Map<String, String> properties){
        this.properties = properties;
        agent = new EmbeddedAgent("myagent");
        agent.configure(this.properties);
        agent.start();
    }

    public void sendDataToFlume(List<String> datas){
        final List<Event> events = new ArrayList<Event>();
        datas.forEach(data -> events.add(EventBuilder.withBody(data, Charset.forName("UTF-8"))));
        try {
            agent.putAll(events);
        } catch (EventDeliveryException e) {
            e.printStackTrace();
        }
    }

    public void sendTransaction(){
        Channel ch = new MemoryChannel();
        Transaction txn = ch.getTransaction();
        txn.begin();
        try {
            // This try clause includes whatever Channel operations you want to do

            Event eventToStage = EventBuilder.withBody("Hello Flume!",
                    Charset.forName("UTF-8"));
            ch.put(eventToStage);
            // Event takenEvent = ch.take();
            // ...
            txn.commit();
        } catch (Throwable t) {
            txn.rollback();

            // Log exception, handle individual exceptions as needed

            // re-throw all Errors
            if (t instanceof Error) {
                throw (Error)t;
            }
        } finally {
            txn.close();
        }
    }

    public void cleanUp(){
        agent.stop();
    }

}
