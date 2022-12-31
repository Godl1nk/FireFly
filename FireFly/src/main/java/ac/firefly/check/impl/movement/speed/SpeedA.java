package ac.firefly.check.impl.movement.speed;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import ac.firefly.data.PlayerData;
import ac.firefly.managers.PluginManager;
import ac.firefly.util.interaction.BlockUtils;
import ac.firefly.util.interaction.PlayerUtils;

import ac.firefly.util.interaction.NEW_Velocity_Utils;
import ac.firefly.util.interaction.TimerUtils;
import ac.firefly.util.math.MathUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

public class SpeedA extends Check {

    public SpeedA() {
        super("Speed (A)", CheckType.MOVEMENT, true);
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
            if (MathUtils.elapsed(data.getLastVelMS(), 250L)) {
                int verbose = data.getSpeedAVerbose();
                final double speedEffect = PlayerUtils.getPotionEffectLevel(p, PotionEffectType.SPEED);
                double speedAThreshold = ((data.getAirTicks() > 0) ? ((data.getAirTicks() >= 6) ? ((data.getAirTicks() == 13) ? 0.466 : 0.35) : (0.345 * Math.pow(0.986938064, data.getAirTicks()))) : ((data.getGroundTicks() > 5) ? 0.362 : ((data.getGroundTicks() == 3) ? 0.62 : 0.4))) + ((data.getAirTicks() > 0) ? (-0.001 * data.getAirTicks() + 0.014) : ((0.018 - ((data.getGroundTicks() >= 6) ? 0.0 : (data.getGroundTicks() * 0.001))) * speedEffect));
                speedAThreshold = ((data.getAboveBlockTicks() > 0) ? (speedAThreshold + 0.25) : speedAThreshold);
                speedAThreshold = ((data.getIceTicks() > 0) ? (speedAThreshold + 0.14) : speedAThreshold);
                speedAThreshold = ((data.getSlimeTicks() > 0) ? (speedAThreshold + 0.1) : speedAThreshold);
                speedAThreshold = ((data.getIceTicks() > 0 && data.getAboveBlockTicks() > 0) ? (speedAThreshold + 0.24) : speedAThreshold);
                if (BlockUtils.isHalfBlock(p.getLocation().add(0.0, -1.5, 0.0).getBlock()) || BlockUtils.isNearHalfBlock(p) || BlockUtils.isStair(p.getLocation().add(0.0, 1.5, 0.0).getBlock()) || BlockUtils.isNearStair(p) || NEW_Velocity_Utils.didTakeVel(p) || PlayerUtils.wasOnSlime(p)) {
                    return;
                }
                if (speed > speedAThreshold) {
                    verbose += 8;
                } else {
                    verbose = ((verbose > 0) ? (verbose - 1) : 0);
                }
                if (verbose > 38) {
//                    SetBackSystem.setBack(p);
                    //verbose = 0;
                }
                data.setSpeedAVerbose(verbose);
            } else {
                data.setSpeedAVerbose(0);
            }
        }
    }
}
