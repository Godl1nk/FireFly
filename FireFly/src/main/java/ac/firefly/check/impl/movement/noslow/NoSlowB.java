package ac.firefly.check.impl.movement.noslow;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import ac.firefly.util.math.MathUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NoSlowB extends Check {

    public static Map<UUID, Map.Entry<Integer, Long>> speedTicks;

    // Web Check //

    public NoSlowB() {
        super("NoSlow (B)", CheckType.MOVEMENT, true);

        speedTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent e) {
        if (speedTicks.containsKey(e.getPlayer().getUniqueId())) {
            speedTicks.remove(e.getPlayer().getUniqueId());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onMove(PlayerMoveEvent e) {
        if (e.getTo().getX() == e.getFrom().getX() && e.getFrom().getY() == e.getTo().getY()
                && e.getTo().getZ() == e.getFrom().getZ()) {
            return;
        }
        Player player = e.getPlayer();
        double OffsetXZ = MathUtils.offset(MathUtils.getHorizontalVector(e.getFrom().toVector()),
                MathUtils.getHorizontalVector(e.getTo().toVector()));

        if (!player.getLocation().getBlock().getType().equals(Material.WEB)) {
            return;
        }

        if (OffsetXZ < 0.2) {
            return;
        }

        flag(player, "B", null);
    }


}