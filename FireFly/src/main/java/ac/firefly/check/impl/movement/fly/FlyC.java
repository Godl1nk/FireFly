package ac.firefly.check.impl.movement.fly;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import ac.firefly.managers.PluginManager;
import ac.firefly.util.interaction.*;
import ac.firefly.data.PlayerData;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class FlyC extends Check {
    public FlyC() {
        super("Fly (C)", CheckType.MOVEMENT, true);
    }

    private static void setBackPlayer(Player p) {
        SetBackSystem.setBack(p);
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
                || PlayerUtils.isInWeb(p)
                || PlayerUtils.isOnClimbable(p, 1)
                || NEW_Velocity_Utils.didTakeVel(p)
                || VelocityUtils.didTakeVelocity(p)) {
            return;
        }

        PlayerData data = PluginManager.instance.getDataManager().getData(p);

        if (data == null) {
            return;
        }

        //Glide check
        if (PlayerUtils.getDistanceToGround(p) > 3) {
            double OffSet = e.getFrom().getY() - e.getTo().getY();
            long Time = System.currentTimeMillis();
            if (OffSet <= 0.0 || OffSet > 0.16) {
                data.setGlideTicks(0);
                return;
            }
            if (data.getGlideTicks() != 0) {
                Time = data.getGlideTicks();
            }
            long Millis = System.currentTimeMillis() - Time;
            if (Millis > 200L) {
                if (BlockUtils.isNearLiquid(p)
                        || PlayerUtils.wasOnSlime(p))
                    return;
                flag(p, "C", "Time: " + Time);
                setBackPlayer(p);
                data.setGlideTicks(0);
            }
            data.setGlideTicks(Time);
        } else {
            data.setGlideTicks(0);
        }
    }

    private int getDistanceToGround(Player p) {
        Location loc = p.getLocation().clone();
        double y = loc.getBlockY();
        int distance = 0;
        for (double i = y; i >= 0; i--) {
            loc.setY(i);
            if (loc.getBlock().getType().isSolid() || loc.getBlock().isLiquid()) break;
            distance++;
        }
        return distance;
    }
}
