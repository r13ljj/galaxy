package disruptor;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;

import java.io.Serializable;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <pre>
 *
 *  File: DisruptorTest.java
 *
 *  Copyright (c) 2018, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,					Who,					What;
 *  2018/4/4				lijunjun				Initial.
 *
 * </pre>
 */
public class DisruptorTest {

    private final static int RING_BUFFER_SIZE = 2 << 1;
    private final static AtomicInteger counter = new AtomicInteger(0);

    private Disruptor<DssEvent> disruptor = null;
    private DssEventTranslator translator = null;
    private RingBuffer<DssEvent> buffer = null;

    public static void main(String[] args) throws Exception{
        DisruptorTest test = new DisruptorTest();
        for(int i=0; i<1000; i++){
            test.doSome(new DssEventExecutor());
        }
    }

    public DisruptorTest() {
        initial();
    }

    private void initial(){
        disruptor = new Disruptor<DssEvent>(new DssEventFactory(), RING_BUFFER_SIZE,
                DaemonThreadFactory.INSTANCE, ProducerType.SINGLE, new LiteBlockingWaitStrategy());
        disruptor.handleEventsWith(new DisruptorEventHandler());
        buffer = disruptor.start();
        translator = new DssEventTranslator();
    }

    public void doSome(DssEventExecutor eventExecutor){
        //long sequence = buffer.next();
        //System.out.println("do some sequence:"+sequence);
        try {
            disruptor.publishEvent(translator, eventExecutor);
        } finally {
            //buffer.publish(sequence);
        }
    }

    public void close(){
        if (disruptor != null){
            disruptor.shutdown();
        }
    }

    /**
     *
     */
    class DssEvent{
        private DssEventExecutor value;

        public DssEvent() {
        }

        public DssEventExecutor getValue() {
            return value;
        }

        public void setValue(DssEventExecutor value) {
            this.value = value;
        }
    }

    /**
     *
     */
    static class DssEventExecutor implements Runnable{

        private String name;

        public DssEventExecutor(){
            this.name = "DssEventExecutor"+counter.getAndIncrement();
        }

        public void run() {
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println(name+" run over.");
        }
    }

    /**
     *
     */
    class DssEventFactory implements EventFactory<DssEvent> {
        public DssEvent newInstance() {
            return new DssEvent();
        }
    }

    /**
     *
     */
    class DisruptorEventHandler implements EventHandler<DssEvent>{

        public void onEvent(DssEvent event, long l, boolean b) throws Exception {
            System.out.println("disruptor event handler var1:"+l+" var2:"+b);
            long start = System.currentTimeMillis();
            DssEventExecutor eventExecutor = event.getValue();
            eventExecutor.run();
            System.out.println("disruptor event handler ct:"+(System.currentTimeMillis()-start)+"ms");
            // 注意：这里要置为 null, 这个跟disruptor 本身的设计有关系
            //event.setValue(null);
        }
    }

    class DssEventTranslator implements EventTranslatorOneArg<DssEvent, DssEventExecutor>{

        @Override
        public void translateTo(DssEvent dssEvent, long l, DssEventExecutor eventExecutor) {
            System.out.println("disruptor event translator var1:"+l);
            dssEvent.setValue(eventExecutor);
        }
    }

}
