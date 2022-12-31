package ac.firefly.check.impl.movement.speed;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import ac.firefly.managers.PluginManager;
import ac.firefly.util.interaction.*;
import ac.firefly.data.PlayerData;
import ac.firefly.util.math.MathUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

public class SpeedB extends Check {

    public SpeedB() {
        super("Speed (B)", CheckType.MOVEMENT, true);
    }

    @EventHandler
    public void onMove(final PlayerMoveEvent e) {
        final Player p = e.getPlayer();
        if (p.hasPotionEffect(PotionEffectType.SPEED)) {
            return;
        }
        final PlayerData data = PluginManager.instance.getDataManager().getData(p);
        final Location to = e.getTo();
        final Location from = e.getFrom();
        if ((to.getX() == from.getX() && to.getY() == from.getY() && to.getZ() == from.getZ()) || p.getAllowFlight()) {
            return;
        }
        if (data != null) {
            if (data.isSpeed_PistonExpand_Set() && TimerUtils.elapsed(data.getSpeed_PistonExpand_MS(), 9900L)) {
                data.setSpeed_PistonExpand_Set(false);
            }

            final double speed = MathUtils.getHorizontalDistance(from, to);
            final Location l = p.getLocation();
            final int x = l.getBlockX();
            final int y = l.getBlockY();
            final int z = l.getBlockZ();
            final Location blockLoc = new Location(p.getWorld(), (double) x, (double) (y - 1), (double) z);
            final Location loc = new Location(p.getWorld(), (double) x, (double) y, (double) z);
            final Location loc2 = new Location(p.getWorld(), (double) x, (double) (y + 1), (double) z);
            final Location above = new Location(p.getWorld(), (double) x, (double) (y + 2), (double) z);
            final Location above2 = new Location(p.getWorld(), (double) (x - 1), (double) (y + 2), (double) (z - 1));
            double MaxAirSpeed = 0.4;
            double maxSpeed = 0.42;
            double MaxSpeedNEW = 0.75;
            if (data.isNearIce()) {
                MaxSpeedNEW = 1.0;
            }
            final double Max = 0.28;
            if (p.hasPotionEffect(PotionEffectType.SPEED)) {
                final int level = PlayerUtils.getPotionEffectLevel(p, PotionEffectType.SPEED);
                if (level > 0) {
                    maxSpeed *= level * 20 * 0.011 + 1.0;
                    MaxAirSpeed *= level * 20 * 0.011 + 1.0;
                    maxSpeed *= level * 20 * 0.011 + 1.0;
                    MaxSpeedNEW *= level * 20 * 0.011 + 1.0;
                }
            }
            MaxAirSpeed += ((p.getWalkSpeed() > 0.2) ? (p.getWalkSpeed() * 0.8) : 0.0);
            maxSpeed += ((p.getWalkSpeed() > 0.2) ? (p.getWalkSpeed() * 0.8) : 0.0);
            if (!PlayerUtils.isOnGround4(p) && speed >= MaxAirSpeed && !data.isNearIce() && blockLoc.getBlock().getType() != Material.ICE && !blockLoc.getBlock().isLiquid() && !loc.getBlock().isLiquid() && blockLoc.getBlock().getType() != Material.PACKED_ICE && above.getBlock().getType() == Material.AIR && above2.getBlock().getType() == Material.AIR && blockLoc.getBlock().getType() != Material.AIR && !NEW_Velocity_Utils.didTakeVel(p) && !BlockUtils.isNearStair(p)) {
                if (!VelocityUtils.didTakeVelocity(p) && !NEW_Velocity_Utils.didTakeVel(p) && PlayerUtils.getDistanceToGround(p) <= 1) {
                    if (data.getSpeed2Verbose() >= 8 || (!VelocityUtils.didTakeVelocity(p) && !NEW_Velocity_Utils.didTakeVel(p) && p.getLocation().add(0.0, 1.94, 0.0).getBlock().getType() != Material.AIR)) {
                        this.flag(p, "(B)", "Verbose: " + data.getSpeed2Verbose());
                        SetBackSystem.setBack(p);
                    } else {
                        data.setSpeed2Verbose(data.getSpeed2Verbose() + 1);
                    }
                } else {
                    data.setSpeed2Verbose(0);
                }
            }
            final double onGroundDiff = to.getY() - from.getY();
            if (speed > Max && !PlayerUtils.isAir(p) && onGroundDiff <= -0.4 && p.getFallDistance() <= 0.4 && blockLoc.getBlock().getType() != Material.ICE && e.getTo().getY() != e.getFrom().getY() && blockLoc.getBlock().getType() != Material.PACKED_ICE && loc2.getBlock().getType() != Material.TRAP_DOOR && above.getBlock().getType() == Material.AIR && above2.getBlock().getType() == Material.AIR && data.getAboveBlockTicks() != 0) {
                this.flag(p, "(B)", "" + data.getAboveBlockTicks());
                SetBackSystem.setBack(p);
            }
        }
    }
}
