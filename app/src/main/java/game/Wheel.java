package game;

import java.util.concurrent.ThreadLocalRandom;

public class Wheel {
    public static final int BANKRUPT = -1;
    public static final int SKIP = 0;
    private final int[] segments = {50, 100, 200, 250, 500, 750, 1000, BANKRUPT, SKIP};

    public int spin() {
        int index = ThreadLocalRandom.current().nextInt(segments.length);
        return segments[index];
    }
}
