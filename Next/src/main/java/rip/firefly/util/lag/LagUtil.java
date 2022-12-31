package rip.firefly.util.lag;

import rip.firefly.bridge.task.LagTask;

public class LagUtil {
    public static double getTPS() {
        return LagTask.getTPS();
    }
}
