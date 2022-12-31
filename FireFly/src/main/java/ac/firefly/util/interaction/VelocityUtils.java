package ac.firefly.util.interaction;

import ac.firefly.data.PlayerData;
import ac.firefly.managers.PluginManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerVelocityEvent;

public class VelocityUtils implements Listener {
    public static boolean didTakeVelocity(Player p) {
        boolean out = false;
        PlayerData data = PluginManager.instance.getDataManager().getData(p);
        if (data != null && data.isDidTakeVelocity()) {
            out = true;
        }
        return out;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        PlayerData data = PluginManager.instance.getDataManager().getData(p);
        if (data != null) {
            if (data.isDidTakeVelocity()) {
                if (TimerUtils.elapsed(data.getLastVelMS(), 300L)) {
                    data.setDidTakeVelocity(false);
                }
            }
        }
    }

    @EventHandler
    public void onVelEvent(PlayerVelocityEvent e) {
        Player p = e.getPlayer();
        PlayerData data = PluginManager.instance.getDataManager().getData(p);
        if (data != null) {
            data.setDidTakeVelocity(true);
            data.setLastVelMS(TimerUtils.nowlong());
        }
    }
}
