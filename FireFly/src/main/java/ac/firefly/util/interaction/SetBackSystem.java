package ac.firefly.util.interaction;

import ac.firefly.data.PlayerData;
import ac.firefly.managers.ConfigManager;
import ac.firefly.managers.PluginManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;


public class SetBackSystem implements Listener {
    public static final String BLOCK = null;

    public static void setBack(Player p) {
        if (p.hasPermission("firefly.bypass")) {
            return;
        }
        if (ConfigManager.settings.getConfiguration().getBoolean("silent")) {
            return;
        }
        PlayerData data = PluginManager.instance.getDataManager().getData(p);
        if (data != null) {
            if (!data.isShouldSetBack()) {
                data.setShouldSetBack(true);
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        PlayerData data = PluginManager.instance.getDataManager().getData(p);
        if (data != null) {
            if (data.isShouldSetBack()) {
                if (data.getSetBackTicks() >= 5) {
                    Location setback = data.getSetbackLocation() != null ? data.getSetbackLocation() : e.getFrom();
                    e.setTo(setback);
                    data.setShouldSetBack(false);
                } else {
                    Location setback = data.getSetbackLocation() != null ? data.getSetbackLocation() : e.getFrom();
                    e.setTo(setback);
                    data.setSetBackTicks(data.getSetBackTicks() + 1);
                }
            } else if (PlayerUtils.isOnGround(p)) {
                data.setSetbackLocation(e.getFrom());
            }
        }
    }
}
