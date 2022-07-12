package example01;

import com.lmax.disruptor.EventTranslatorOneArg;

import java.nio.ByteBuffer;

/**
 * @author yangsong
 * @version 1.0
 * @date 2022/7/11 12:27
 */
public class LongEventTranslatorOneArg implements EventTranslatorOneArg<LongEvent, ByteBuffer> {

    @Override
    public void translateTo(LongEvent event, long sequence, ByteBuffer buffer) {
        event.set(buffer.getLong(0));
    }

}