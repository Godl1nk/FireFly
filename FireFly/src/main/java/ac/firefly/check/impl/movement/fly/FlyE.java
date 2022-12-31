package ac.firefly.check.impl.movement.fly;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import ac.firefly.util.interaction.*;
import ac.firefly.util.math.MathUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FlyE extends Check {

    public static Map<UUID, Long> flyTicksE;

    public FlyE() {
        super("Fly (E)", CheckType.MOVEMENT, true);
        flyTicksE = new HashMap<>();
    }

    private static void setBackPlayer(Player p) {
        SetBackSystem.setBack(p);
    }

    @EventHandler
    public void onLog(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();

        flyTicksE.remove(uuid);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Location from = e.getFrom();
        Location to = e.getTo();
        Player p = e.getPlayer();
        if (p.getGameMode().equals(GameMode.CREATIVE)
                || p.getAllowFlight()
                || e.getPlayer().getVehicle() != null
                || p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.SPONGE
                || PlayerUtils.isOnClimbable(p, 0)
                || PlayerUtils.isOnClimbable(p, 1)
                || NEW_Velocity_Utils.didTakeVel(p)
                || VelocityUtils.didTakeVelocity(p)
                || e.isCancelled()
                || PlayerUtils.isInWeb(p)
                || PlayerUtils.isInWater(p)
                || p.getVehicle() != null
                || (e.getTo().getX() == e.getFrom().getX()) && (e.getTo().getZ() == e.getFrom().getZ())) {
            return;
        }

        if (PlayerUtils.blocksNear(p.getLocation())) {
            flyTicksE.remove(p.getUniqueId());
            return;
        }
        if (Math.abs(e.getTo().getY() - e.getFrom().getY()) > 0.06) {
            flyTicksE.remove(p.getUniqueId());
            return;
        }

        long Time = System.currentTimeMillis();
        if (flyTicksE.containsKey(p.getUniqueId())) {
            Time = flyTicksE.get(p.getUniqueId());
        }
        long MS = System.currentTimeMillis() - Time;
        if (MS > 200L) {
            flag(p, "E", "Hovering for " + MathUtils.trim(1, (double) (MS / 1000)) + " second(s)");
            flyTicksE.remove(p.getUniqueId());
            return;
        }
        flyTicksE.put(p.getUniqueId(), Time);
    }
}
