package rip.firefly.check.impl.movement.speed;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.PacketCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.packet.tinyprotocol.api.NMSObject;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInFlyingPacket;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInUseEntityPacket;
import rip.firefly.util.location.PlayerLocation;
import rip.firefly.util.math.MathUtil;
import rip.firefly.util.math.TimerUtil;
import rip.firefly.util.player.PlayerUtil;
import rip.firefly.util.velocity.VelocityUtil;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@CheckData(name = "Speed", subType = "C", description = "Checks For A Player Going Over The Speed Limit", type = CheckType.MOVEMENT, experimental = true, threshold = 7)
public class SpeedC extends PacketCheck {

    public static Map<UUID, Map.Entry<Integer, Long>> speedTicks = new HashMap<>();
    public static Map<UUID, Map.Entry<Integer, Long>> tooFastTicks = new HashMap<>();
    public static Map<UUID, Long> lastHit = new HashMap<>();
    public static Map<UUID, Double> velocity = new HashMap<>();

    public SpeedC(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(PlayerData playerData, NMSObject fpacket) {
        if (fpacket instanceof WrappedInFlyingPacket) {
            Player player = Bukkit.getPlayer(playerData.getUuid());
            if(player.isFlying() || player.getAllowFlight()) {
                return;
            }
            if ((playerData.getFrom().getX() == playerData.getTo().getX()) && (playerData.getFrom().getY() == playerData.getTo().getY())
                    && (playerData.getFrom().getZ() == playerData.getFrom().getZ())
                    || player.getAllowFlight()
                    || player.getVehicle() != null
                    || player.getVelocity().length() + 0.1 < velocity.getOrDefault(player.getUniqueId(), -1.0D)
                    || VelocityUtil.didTakeVel(player)
                    || player.isFlying()
                    || (VelocityUtil.didTakeVel(player)
                    && !player.hasPotionEffect(PotionEffectType.POISON)
                    && !player.hasPotionEffect(PotionEffectType.WITHER) && player.getFireTicks() == 0)) return;

            long lastHitDiff = lastHit.containsKey(player.getUniqueId())
                    ? lastHit.get(player.getUniqueId()) - System.currentTimeMillis()
                    : 2001L;

            int count = 0;
            long Time = TimerUtil.nowlong();
            if (speedTicks.containsKey(player.getUniqueId())) {
                count = speedTicks.get(player.getUniqueId()).getKey();
                Time = speedTicks.get(player.getUniqueId()).getValue();
            }
            int tooFastCount = 0;
            double percent = 0D;
            if (tooFastTicks.containsKey(player.getUniqueId())) {
                double OffsetXZ = MathUtil.offset(MathUtil.getHorizontalVector(playerData.getFrom().toVector()),
                        MathUtil.getHorizontalVector(playerData.getTo().toVector()));
                double limitXZ = 0.0D;
                if ((PlayerUtil.isOnGround(player.getLocation())) && (player.getVehicle() == null)) {
                    limitXZ = 0.34D;
                } else {
                    limitXZ = 0.39D;
                }
                if (lastHitDiff < 800L) {
                    ++limitXZ;
                } else if (lastHitDiff < 1600L) {
                    limitXZ += 0.4;
                } else if (lastHitDiff < 2000L) {
                    limitXZ += 0.1;
                }
             //   if (PlayerUtil.hasSlabsNear(player.getLocation())) {
                if (PlayerUtil.slabsNear(new PlayerLocation(player.getLocation()), player)) {
                    limitXZ += 0.05D;
                }
                Location b = PlayerUtil.getEyeLocation(player).clone();
                b.add(0.0D, 1.0D, 0.0D);
                if ((b.getBlock().getType() != Material.AIR) && (!canStandWithin(b.getBlock()))) {
                    limitXZ = 0.69D;
                }
                Location below = Bukkit.getPlayer(playerData.getUuid()).getLocation().clone().add(0.0D, -1.0D, 0.0D);

                if (PlayerUtil.isStair(below.getBlock())) {
                    limitXZ += 0.6;
                }

                if (isOnIce(player)) {
                    if ((b.getBlock().getType() != Material.AIR) && (!canStandWithin(b.getBlock()))) {
                        limitXZ = 2.0D;
                    } else {
                        limitXZ = 0.75D;
                    }
                }
                float speed = player.getWalkSpeed();
                limitXZ += (speed > 0.2F ? speed * 10.0F * 0.33F : 0.0F);
                for (PotionEffect effect : player.getActivePotionEffects()) {
                    if (effect.getType().equals(PotionEffectType.SPEED)) {
                        if (player.isOnGround()) {
                            limitXZ += 0.061D * (effect.getAmplifier() + 1);
                        } else {
                            limitXZ += 0.031D * (effect.getAmplifier() + 1);
                        }
                    }
                }
                if (OffsetXZ > limitXZ && !TimerUtil.elapsed(tooFastTicks.get(player.getUniqueId()).getValue(), 150L)) {
                    percent = (OffsetXZ - limitXZ) * 100;
                    tooFastCount = tooFastTicks.get(player.getUniqueId()).getKey()
                            + 3;
                } else {
                    tooFastCount = tooFastCount > -150 ? tooFastCount-- : -150;
                }
            }
            if (tooFastCount >= 11) {
                tooFastCount = 0;
                count++;
            }
            if (speedTicks.containsKey(player.getUniqueId()) && TimerUtil.elapsed(Time, 30000L)) {
                count = 0;
                Time = TimerUtil.nowlong();
            }
            if (count >= 3) {
                count = 0;
                flag(playerData, String.format("FP: %s", (Math.round(percent) + "%")), String.format("C: %s", count));
            }
            if (!PlayerUtil.isOnGround(player.getLocation())) {
                velocity.put(player.getUniqueId(), player.getVelocity().length());
            } else {
                velocity.put(player.getUniqueId(), -1.0D);
            }
            tooFastTicks.put(player.getUniqueId(),
                    new AbstractMap.SimpleEntry<>(tooFastCount, System.currentTimeMillis()));
            speedTicks.put(player.getUniqueId(),
                    new AbstractMap.SimpleEntry<>(count, Time));
        } else if(fpacket instanceof WrappedInUseEntityPacket && ((WrappedInUseEntityPacket)fpacket).getAction() == WrappedInUseEntityPacket.EnumEntityUseAction.ATTACK) {
            WrappedInUseEntityPacket packet = (WrappedInUseEntityPacket)fpacket;

            if (packet.getEntity() instanceof Player) {
                Player player = (Player) packet.getEntity();

                lastHit.put(player.getUniqueId(), System.currentTimeMillis());
            }
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

    /*@EventHandler(priority = EventPriority.MONITOR)
    public void onLog(PlayerQuitEvent e) {
        speedTicks.remove(e.getPlayer().getUniqueId());
        tooFastTicks.remove(e.getPlayer().getUniqueId());
        lastHit.remove(e.getPlayer().getUniqueId());
        velocity.remove(e.getPlayer().getUniqueId());
    }*/

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