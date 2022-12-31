package rip.firefly.bridge;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import rip.firefly.bridge.data.BridgeData;
import rip.firefly.bridge.task.LagTask;
import rip.firefly.bridge.util.BridgeUtil;

public class Bridge {

    @Getter private static String bridgeIp = "108.51.159.3:4205";

    public static void loadBridge(JavaPlugin fireFly) {
        Bukkit.getScheduler().runTaskTimer(fireFly, new LagTask(), 1L, 1L);

        BridgeUtil.startup();

    }

    public static void unloadBridge(JavaPlugin fireFly) {

       BridgeUtil.shutdown();

    }
}
