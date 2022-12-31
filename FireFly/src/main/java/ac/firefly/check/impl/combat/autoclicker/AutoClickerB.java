package ac.firefly.check.impl.combat.autoclicker;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import ac.firefly.data.PlayerData;
import ac.firefly.events.packet.PacketSwingArmEvent;
import ac.firefly.managers.PluginManager;
import ac.firefly.util.interaction.Lag;
import ac.firefly.util.interaction.TimerUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public class AutoClickerB extends Check {
    public Map<UUID, Integer> clicks;
    private Map<UUID, Long> recording;

    public AutoClickerB() {
        super("AutoClicker (B)", CheckType.COMBAT, true);

        clicks = new HashMap<>();
        recording = new HashMap<>();
    }

    @EventHandler
    public void onLog(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        clicks.remove(uuid);
        recording.remove(uuid);
    }

    @EventHandler
    public void onSwing(PacketSwingArmEvent e) {
        Player player = e.getPlayer();
        if (/*Lag.getTPS() < 17 || */Lag.getPing(player) > 100)
            return;
        PlayerData data = PluginManager.instance.getDataManager().getData(player);
        if(data.isDigging()) {
            return;
        }
        int clicks = this.clicks.getOrDefault(player.getUniqueId(), 0);
        long time = recording.getOrDefault(player.getUniqueId(), System.currentTimeMillis());
        if (TimerUtils.elapsed(time, 1000L)) {
            if (clicks > 40) {
                flag(player, clicks + " Clicks/Second", clicks + " Clicks/Second" );
            }
            clicks = 0;
            recording.remove(player.getUniqueId());
        } else {
            clicks++;
        }
        this.clicks.put(player.getUniqueId(), clicks);
        recording.put(player.getUniqueId(), time);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onLeave(PlayerQuitEvent e) {
        clicks.remove(e.getPlayer().getUniqueId());
        recording.remove(e.getPlayer().getUniqueId());
    }
}
