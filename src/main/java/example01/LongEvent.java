package example01;

import lombok.Data;

/**
 * @author yangsong
 * @version 1.0
 * @date 2022/7/11 12:27
 */
public class LongEvent {

    private long value;

    public void set(long value) {
        this.value = value;
    }

    public long get() {
        return this.value;
    }

}