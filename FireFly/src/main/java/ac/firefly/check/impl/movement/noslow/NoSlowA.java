package ac.firefly.check.impl.movement.noslow;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public class NoSlowA extends Check {

    public static Map<UUID, Map.Entry<Integer, Long>> speedTicks;

    // Bow Check //
    
    public NoSlowA() {
        super("NoSlow (A)", CheckType.MOVEMENT, true);

        speedTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent e) {
        if (speedTicks.containsKey(e.getPlayer().getUniqueId())) {
            speedTicks.remove(e.getPlayer().getUniqueId());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void BowShoot(final EntityShootBowEvent event) {
        if (!this.isEnabled()) {
            return;
        }
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        final Player player = (Player) event.getEntity();
        if (player.isInsideVehicle()) {
            return;
        }
        if (player.isSprinting()) {
            flag(player, "Bow", null);
        }
    }
}