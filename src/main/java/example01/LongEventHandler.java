package example01;

import com.lmax.disruptor.EventHandler;

import java.util.Date;

/**
 * @author yangsong
 * @version 1.0
 * @date 2022/7/11 12:27
 */
public class LongEventHandler implements EventHandler<LongEvent> {
    @Override
    public void onEvent(LongEvent event, long sequence, boolean endOfBatch) {
        System.out.println(new Date() + ":Event-" + event.get());
    }
}