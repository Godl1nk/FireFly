package rip.firefly.manager;

import org.bukkit.plugin.java.JavaPlugin;
import rip.firefly.handler.*;

public class HandlerManager {

    public HandlerManager(JavaPlugin plugin) {
        new BukkitHandler(plugin);
        new ClientHandler(plugin);
        new CombatHandler(plugin);
        new HiderHandler(plugin);
        new MovementHandler(plugin);
        new PacketHandler(plugin);
    }
}
