package example01;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.nio.ByteBuffer;
import java.util.concurrent.Executors;

/**
 * @author yangsong
 * @version 1.0
 * @date 2022/7/11 12:27
 */
public class LongEventMain {

    public static void main(String[] args) throws Exception {
        int bufferSize = 1024;
        final Disruptor<LongEvent> disruptor = new Disruptor<>(
                new LongEventFactory(),
                bufferSize,
                Executors.defaultThreadFactory(),
                ProducerType.SINGLE,
                new YieldingWaitStrategy()
        );

        disruptor.handleEventsWith(new LongEventHandler());
        disruptor.start();


        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
        ByteBuffer bb = ByteBuffer.allocate(8);
        for (long l = 0; true; l++) {
            bb.putLong(0, l);
            ringBuffer.publishEvent(new LongEventTranslatorOneArg(), bb);
            Thread.sleep(1000);
        }
    }

}