package rip.firefly.util.math;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author OmenDoesStuff
 */
public class RandomUtil {

    /**
     * Generates a random integer within
     * the set bounds.
     *
     * @param min The minimum integer size
     * @param max The maximum integer size
     * @return The random integer
     */
    public static int randomIntWithinBounds(int min, int max) {
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        int randomNum = threadLocalRandom.nextInt(min, max + 1);
        return randomNum;
    }

    /**
     * Generates a random integer within
     * the set bounds.
     *
     * @param min The minimum integer size
     * @param max The maximum integer size
     * @return The random integer
     */
    public static long randomLongWithinBounds(int min, int max) {
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        long randomNum = threadLocalRandom.nextLong(min, max + 1);
        return randomNum;
    }
}
