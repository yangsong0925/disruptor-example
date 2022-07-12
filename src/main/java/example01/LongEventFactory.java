package example01;

import com.lmax.disruptor.EventFactory;

/**
 * @author yangsong
 * @version 1.0
 * @date 2022/7/11 12:27
 */
public class LongEventFactory implements EventFactory<LongEvent> {
    @Override
    public LongEvent newInstance() {
        return new LongEvent();
    }
}
