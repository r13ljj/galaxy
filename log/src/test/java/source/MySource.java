package source;

import org.apache.flume.*;
import org.apache.flume.conf.Configurable;
import org.apache.flume.event.EventBuilder;
import org.apache.flume.source.AbstractSource;

import java.nio.charset.Charset;

/**
 * <pre>
 *
 *  File: MySource.java
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
public class MySource extends AbstractSource implements Configurable, PollableSource {
    @Override
    public long getBackOffSleepIncrement() {
        return 1;
    }

    @Override
    public long getMaxBackOffSleepInterval() {
        return 3000;
    }

    private String myProp;

    @Override
    public void configure(Context context) {
        String myProp = context.getString("myProp", "defaultValue");

        // Process the myProp value (e.g. validation, convert to another type, ...)

        // Store myProp for later retrieval by process() method
        this.myProp = myProp;
    }

    @Override
    public void start() {
        // Initialize the connection to the external client
    }

    @Override
    public void stop () {
        // Disconnect from external client and do any additional cleanup
        // (e.g. releasing resources or nulling-out field values) ..
    }

    @Override
    public Status process() throws EventDeliveryException {
        Status status = null;
        Transaction txn = null;
        txn.begin();
        try {
            // This try clause includes whatever Channel/Event operations you want to do

            // Receive new data
            Event e = getSomeData();

            // Store the Event into this Source's associated Channel(s)
            getChannelProcessor().processEvent(e);

            status = Status.READY;
        } catch (Throwable t) {
            // Log exception, handle individual exceptions as needed

            status = Status.BACKOFF;

            // re-throw all Errors
            if (t instanceof Error) {
                throw (Error)t;
            }
        } finally {
            txn.close();
        }
        return status;
    }

    private Event getSomeData(){
        return EventBuilder.withBody("xxx", Charset.forName("UTF-8"));
    }
}
