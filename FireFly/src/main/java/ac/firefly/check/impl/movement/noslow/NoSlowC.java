package ac.firefly.check.impl.movement.noslow;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NoSlowC extends Check {

    public static Map<UUID, Map.Entry<Integer, Long>> speedTicks;

    // Sword Check //

    public NoSlowC() {
        super("NoSlow (C)", CheckType.MOVEMENT, true);

        speedTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent e) {
        if (speedTicks.containsKey(e.getPlayer().getUniqueId())) {
            speedTicks.remove(e.getPlayer().getUniqueId());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onMove(final PlayerMoveEvent event) {
        if (!this.isEnabled()) {
            return;
        }
        final Player player = (Player) event.getPlayer();
        if (player.isInsideVehicle()) {
            return;
        }
        if (player.isSprinting() && player.isBlocking()) {
            flag(player, "C", null);
        }
    }
}