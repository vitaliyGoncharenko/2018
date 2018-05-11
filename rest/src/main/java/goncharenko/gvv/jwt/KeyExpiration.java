package goncharenko.gvv.jwt;

import java.security.Key;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import static java.lang.System.currentTimeMillis;

public class KeyExpiration implements Delayed {
    public final Key key;
    private long lifeTimeMillis;
    private long createTimeMillis;

    public KeyExpiration(long lifeTimeSeconds, Key key) {
        this.key = key;
        this.lifeTimeMillis = lifeTimeSeconds * 1000;
        this.createTimeMillis = System.currentTimeMillis();
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(getDelayMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed delay) {
        return Long.compare(this.getDelayMillis(), ((KeyExpiration) delay).getDelayMillis());
    }

    private long getDelayMillis() {
        return (createTimeMillis + lifeTimeMillis) - currentTimeMillis();
    }
}