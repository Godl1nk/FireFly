package rip.firefly.check.impl.combat.reach;

import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import rip.firefly.FireFly;
import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.PacketCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.packet.tinyprotocol.api.NMSObject;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInKeepAlivePacket;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInUseEntityPacket;
import rip.firefly.packet.tinyprotocol.packet.out.WrappedOutKeepAlivePacket;
import rip.firefly.util.location.CustomLocation;
import rip.firefly.util.location.PastLocation;
import rip.firefly.util.location.RPastLocation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@CheckData(name = "Reach", subType = "B", description = "Checks For Impossible Reach", type = CheckType.COMBAT, threshold = 10)
public class ReachB extends PacketCheck {
    public ReachB(PlayerData data) {
        super(data);

        new BukkitRunnable() {
            public void run() {
                if(lastHit != null) {
                    pastLocations.addLocation(lastHit.getLocation());
                }
            }
        }.runTaskTimer(FireFly.getInstance().getPlugin(), 0L, 1L);
    }

    @Setter int threshold = 10;
    @Setter double maxReach = 3.089;
    PastLocation pastLocations = new PastLocation();
    LivingEntity lastHit;
    @Override
    public void handle(PlayerData data, NMSObject packet) {

        if(packet instanceof WrappedInUseEntityPacket) {
            WrappedInUseEntityPacket wrap = (WrappedInUseEntityPacket) packet;
            Optional<Entity> entityOp = Bukkit.getPlayer(data.getUuid()).getWorld().getEntities().stream().filter(entity -> entity.getEntityId() == wrap.getEntity().getEntityId()).findFirst();

            if (entityOp.isPresent()) {
                Entity entity = entityOp.get();

                WrappedInUseEntityPacket.EnumEntityUseAction action = wrap.getAction();

                if (action.equals(WrappedInUseEntityPacket.EnumEntityUseAction.ATTACK) && entity instanceof LivingEntity) {

                    lastHit = (LivingEntity) entity;
                    Location origin = Bukkit.getPlayer(data.getUuid()).getLocation();

                    List<Vector> pastLocation = pastLocations.getEstimatedLocation(data.getTransactionPing(), 150).stream().map(CustomLocation::toVector).collect(Collectors.toList());

                    float distance = (float) pastLocation.stream().mapToDouble(vec -> vec.clone().setY(0).distance(origin.toVector().clone().setY(0)) - 0.3f).min().orElse(0);

                    if(distance > maxReach) {
                    //    if(incrementBuffer() > 10) {
                        if(incrementBuffer() > threshold) {
                            if(distance < 3.01) {
                                flag(data, "D: " + distance, "M: " + round(maxReach, 2));
                            } else {
                                flag(data, "D: " + round(distance, 2), "M: " + round(maxReach, 2));
                            }
                        }
                      //  }
                    } else decreaseBuffer(getBuffer() > 0 ? 1 : 0);

                }
            }
        }
    }


    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    private double calcDistance(Location location, Location loc) {
        double disX = Math.abs(location.getX() - loc.getX());
        double disZ = Math.abs(location.getZ() - loc.getZ());
        return Math.sqrt(disX * disX + disZ * disZ);
    }
}
