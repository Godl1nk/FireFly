package ac.firefly.check.impl.movement.speed;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import ac.firefly.util.interaction.*;
import ac.firefly.util.math.MathUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SpeedG extends Check {
    public static Map<UUID, Map.Entry<Integer, Long>> speedTicks;
    public static Map<UUID, Map.Entry<Integer, Long>> tooFastTicks;
    public static Map<UUID, Long> lastHit;
    public static Map<UUID, Double> velocity;


    public SpeedG() {
        super("Speed (G)", CheckType.MOVEMENT, true);

        speedTicks = new HashMap<>();
        tooFastTicks = new HashMap<>();
        lastHit = new HashMap<>();
        velocity = new HashMap<>();
    }

    @EventHandler(ignoreCancelled = true)
    public void onHit(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();

            lastHit.put(player.getUniqueId(), System.currentTimeMillis());
        }
    }

    public boolean isOnIce(final Player player) {
        Location a = player.getLocation();
        a.setY(a.getY() - 1.0);
        if (a.getBlock().getType().equals(Material.ICE)) {
            return true;
        }
        a.setY(a.getY() - 1.0);
        return a.getBlock().getType().equals(Material.ICE);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onLog(PlayerQuitEvent e) {
        speedTicks.remove(e.getPlayer().getUniqueId());
        tooFastTicks.remove(e.getPlayer().getUniqueId());
        lastHit.remove(e.getPlayer().getUniqueId());
        velocity.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if ((event.getFrom().getX() == event.getTo().getX()) && (event.getFrom().getY() == event.getTo().getY())
                && (event.getFrom().getZ() == event.getFrom().getZ())
                || player.getAllowFlight()
                || player.getVehicle() != null
                || player.getVelocity().length() + 0.1 < velocity.getOrDefault(player.getUniqueId(), -1.0D)
                || (VelocityUtils.didTakeVelocity(player)
                || NEW_Velocity_Utils.didTakeVel(player)
                && !player.hasPotionEffect(PotionEffectType.POISON)
                && !player.hasPotionEffect(PotionEffectType.WITHER) && player.getFireTicks() == 0)) return;

        long lastHitDiff = lastHit.containsKey(player.getUniqueId())
                ? lastHit.get(player.getUniqueId()) - System.currentTimeMillis()
                : 2001L;

        int Count = 0;
        long Time = TimerUtils.nowlong();
        if (speedTicks.containsKey(player.getUniqueId())) {
            Count = speedTicks.get(player.getUniqueId()).getKey();
            Time = speedTicks.get(player.getUniqueId()).getValue();
        }
        int TooFastCount = 0;
        double percent = 0D;
        if (tooFastTicks.containsKey(player.getUniqueId())) {
            double OffsetXZ = MathUtils.offset(MathUtils.getHorizontalVector(event.getFrom().toVector()),
                    MathUtils.getHorizontalVector(event.getTo().toVector()));
            double LimitXZ = 0.0D;
            if ((PlayerUtils.isOnGround(player)) && (player.getVehicle() == null)) {
                LimitXZ = 0.34D;
            } else {
                LimitXZ = 0.39D;
            }
            if (lastHitDiff < 800L) {
                ++LimitXZ;
            } else if (lastHitDiff < 1600L) {
                LimitXZ += 0.4;
            } else if (lastHitDiff < 2000L) {
                LimitXZ += 0.1;
            }
            if (PlayerUtils.slabsNear(player.getLocation())) {
                LimitXZ += 0.05D;
            }
            Location b = PlayerUtils.getEyeLocation(player);
            b.add(0.0D, 1.0D, 0.0D);
            if ((b.getBlock().getType() != Material.AIR) && (!canStandWithin(b.getBlock()))) {
                LimitXZ = 0.69D;
            }
            Location below = event.getPlayer().getLocation().clone().add(0.0D, -1.0D, 0.0D);

            if (PlayerUtils.isStair(below.getBlock())) {
                LimitXZ += 0.6;
            }

            if (isOnIce(player)) {
                if ((b.getBlock().getType() != Material.AIR) && (!canStandWithin(b.getBlock()))) {
                    LimitXZ = 2.0D;
                } else {
                    LimitXZ = 0.75D;
                }
            }
            float speed = player.getWalkSpeed();
            LimitXZ += (speed > 0.2F ? speed * 10.0F * 0.33F : 0.0F);
            for (PotionEffect effect : player.getActivePotionEffects()) {
                if (effect.getType().equals(PotionEffectType.SPEED)) {
                    if (player.isOnGround()) {
                        LimitXZ += 0.061D * (effect.getAmplifier() + 1);
                    } else {
                        LimitXZ += 0.031D * (effect.getAmplifier() + 1);
                    }
                }
            }
            if (OffsetXZ > LimitXZ && !TimerUtils.elapsed(tooFastTicks.get(player.getUniqueId()).getValue(), 150L)) {
                percent = (OffsetXZ - LimitXZ) * 100;
                TooFastCount = tooFastTicks.get(player.getUniqueId()).getKey()
                        + 3;
            } else {
                TooFastCount = TooFastCount > -150 ? TooFastCount-- : -150;
            }
        }
        if (TooFastCount >= 11) {
            TooFastCount = 0;
            Count++;
        }
        if (speedTicks.containsKey(player.getUniqueId()) && TimerUtils.elapsed(Time, 30000L)) {
            Count = 0;
            Time = TimerUtils.nowlong();
        }
        if (Count >= 3) {
            Count = 0;
            flag(player, Math.round(percent) + "% faster than normal", null);
            SetBackSystem.setBack(player);
        }
        if (!PlayerUtils.isOnGround(player)) {
            velocity.put(player.getUniqueId(), player.getVelocity().length());
        } else {
            velocity.put(player.getUniqueId(), -1.0D);
        }
        tooFastTicks.put(player.getUniqueId(),
                new AbstractMap.SimpleEntry<>(TooFastCount, System.currentTimeMillis()));
        speedTicks.put(player.getUniqueId(),
                new AbstractMap.SimpleEntry<>(Count, Time));
    }

    public static boolean canStandWithin(Block block) {
        boolean isSand = block.getType() == Material.SAND;
        boolean isGravel = block.getType() == Material.GRAVEL;
        boolean solid = (block.getType().isSolid()) && (!block.getType().name().toLowerCase().contains("door"))
                && (!block.getType().name().toLowerCase().contains("fence"))
                && (!block.getType().name().toLowerCase().contains("bars"))
                && (!block.getType().name().toLowerCase().contains("sign"));

        return (!isSand) && (!isGravel) && (!solid);
    }

}
