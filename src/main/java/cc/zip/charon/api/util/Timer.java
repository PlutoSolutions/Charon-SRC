/*
 * Decompiled with CFR 0.150.
 */
package cc.zip.charon.api.util;

public class Timer {
    private long current = -1L;
    private long time = -1L;
    public boolean hasReached(long delay) {
        return System.currentTimeMillis() - this.current >= delay;
    }

    public boolean hasReached(long delay, boolean reset) {
        if (reset) {
            this.reset();
        }
        return System.currentTimeMillis() - this.current >= delay;
    }

    public void reset() {
        this.current = System.currentTimeMillis();
    }
    public long getMs(long time) {
        return time / 1000000L;
    }
    public boolean passedM(double m) { return this.getMs(System.nanoTime() - this.time) >= (long) (m * 1000.0 * 60.0);
    }
    public boolean passedMs(long ms) {
        return this.getMs(System.nanoTime() - this.time) >= ms;
    }
    public void setMs(long ms) {
        this.current = ms;
    }

    public long getTimePassed() {
        return System.currentTimeMillis() - this.current;
    }
}

