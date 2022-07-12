package example02;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.nio.ByteBuffer;
import java.util.concurrent.Executors;

/**
 * @author yangsong
 * @version 1.0
 * @date 2022/7/12 11:48
 */
public class LongEventMain {

    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) throws Exception {
        // 1，构建disruptor
        final Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(
                new LongEventFactory(),
                BUFFER_SIZE,
                Executors.newFixedThreadPool(5), // 【注意点】线程池需要保证足够的线程：有多少个消费者就要有多少个线程，否则有些消费者将不会执行，生产者可能也会一直阻塞下去
                ProducerType.SINGLE,
                new YieldingWaitStrategy()
        );

        EventHandler eventHandler1 = new LongEventHandler1();
        EventHandler eventHandler2 = new LongEventHandler2();
        EventHandler eventHandler3 = new LongEventHandler3();
        EventHandler eventHandler4 = new LongEventHandler4();
        EventHandler eventHandler5 = new LongEventHandler5();

        // 方式1 构建串行执行顺序：
         /*disruptor
         .handleEventsWith(eventHandler1)
         .handleEventsWith(eventHandler2)
         .handleEventsWith(eventHandler3)
         .handleEventsWith(eventHandler4)
         .handleEventsWith(eventHandler5);*/

        // 方式2 构建并行执行顺序
         /*disruptor
         .handleEventsWith(eventHandler1, eventHandler2, eventHandler3, eventHandler4, eventHandler5);*/

        // 方式3 构建菱形执行顺序
         /*disruptor.handleEventsWith(eventHandler1, eventHandler2)
         .handleEventsWith(eventHandler3);*/

        // 2，构建eventHandler执行链
        // 方式4 构建六边形执行顺序
        disruptor.handleEventsWith(eventHandler1, eventHandler3);
        disruptor.after(eventHandler1).handleEventsWith(eventHandler2);
        disruptor.after(eventHandler3).handleEventsWith(eventHandler4);
        disruptor.after(eventHandler2, eventHandler4).handleEventsWith(eventHandler5);

        // 3， 启动disruptor即启动线程池线程执行BatchEventProcessor任务
        disruptor.start();

        // 4，生产者往ringBuffer生产数据并唤醒所有的消费者消费数据
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
        ByteBuffer bb = ByteBuffer.allocate(8);
        bb.putLong(0, 666);
        ringBuffer.publishEvent(new LongEventTranslatorOneArg(), bb);
    }

    static class LongEventTranslatorOneArg implements EventTranslatorOneArg<LongEvent, ByteBuffer> {
        @Override
        public void translateTo(LongEvent event, long sequence, ByteBuffer buffer) {
            event.set(buffer.getLong(0));
        }
    }

    static class LongEvent {
        private long value;

        public void set(long value) {
            this.value = value;
        }

        public long get() {
            return this.value;
        }
    }

    static class LongEventFactory implements EventFactory<LongEvent> {
        @Override
        public LongEvent newInstance() {
            return new LongEvent();
        }
    }

    static class LongEventHandler1 implements EventHandler<LongEvent> {
        @Override
        public void onEvent(LongEvent event, long sequence, boolean endOfBatch) {
            System.out.println("LongEventHandler1-" + event.get() + " executed by " + Thread.currentThread().getName());
        }
    }

    static class LongEventHandler2 implements EventHandler<LongEvent> {
        @Override
        public void onEvent(LongEvent event, long sequence, boolean endOfBatch) {
            System.out.println("LongEventHandler2-" + event.get() + " executed by " + Thread.currentThread().getName());
        }
    }

    static class LongEventHandler3 implements EventHandler<LongEvent> {
        @Override
        public void onEvent(LongEvent event, long sequence, boolean endOfBatch) {
            System.out.println("LongEventHandler3-" + event.get() + " executed by " + Thread.currentThread().getName());
        }
    }

    static class LongEventHandler4 implements EventHandler<LongEvent> {
        @Override
        public void onEvent(LongEvent event, long sequence, boolean endOfBatch) {
            System.out.println("LongEventHandler4-" + event.get() + " executed by " + Thread.currentThread().getName());
        }
    }

    static class LongEventHandler5 implements EventHandler<LongEvent> {
        @Override
        public void onEvent(LongEvent event, long sequence, boolean endOfBatch) {
            System.out.println("LongEventHandler5-" + event.get() + " executed by " + Thread.currentThread().getName());
        }
    }

}
