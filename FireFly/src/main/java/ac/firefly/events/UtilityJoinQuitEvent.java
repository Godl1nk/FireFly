package ac.firefly.events;


import ac.firefly.managers.PluginManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class UtilityJoinQuitEvent implements Listener {


    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent e) {
        PluginManager.instance.getDataManager().addPlayerData(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerQuitEvent e) {
        PluginManager.instance.getDataManager().removePlayerData(e.getPlayer());
    }
}
