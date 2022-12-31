package rip.firefly.check.impl.combat.aim;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.PacketCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.packet.tinyprotocol.api.NMSObject;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInFlyingPacket;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInUseEntityPacket;

import java.lang.reflect.InvocationTargetException;

@CheckData(name = "Aim", subType = "D", description = "Checks If The Entity Is In The Line Of Sight", type = CheckType.COMBAT, threshold = 6, experimental = true)
public class AimD extends PacketCheck {
    public AimD(PlayerData data) {
        super(data);
    }

    private boolean attacked;

    @Override
    public void handle(PlayerData playerData, NMSObject packet) {
        if(packet instanceof WrappedInUseEntityPacket && ((WrappedInUseEntityPacket)packet).getAction() == WrappedInUseEntityPacket.EnumEntityUseAction.ATTACK) {
            attacked = true;
        } else if(packet instanceof WrappedInFlyingPacket) {
            if (!attacked) return;
            attacked = false;

            final Entity target = playerData.getTarget();
            final Entity lastTarget = playerData.getLastTarget();

            if (target != lastTarget) return;

            if (!(target instanceof Player)) return;
            if (playerData.getTargetLocations().size() < 30) return;

            final int now = playerData.getTicks();
            final int latencyInTicks = msToTicks(getPing(Bukkit.getPlayer(playerData.getUuid())));

            final double x = playerData.getX();
            final double z = playerData.getZ();

            final Vector origin = new Vector(x, 0.0, z);

            final double angle = playerData.getTargetLocations().stream()
                    .filter(pair -> Math.abs(now - pair.getY() - latencyInTicks) < 3)
                    .mapToDouble(pair -> {
                        final Vector targetLocation = pair.getX().toVector().setY(0.0);

                        final Vector dirToDestination = targetLocation.clone().subtract(origin);
                        final Vector playerDirection = Bukkit.getPlayer(playerData.getUuid()).getEyeLocation().getDirection().setY(0.0);

                        return dirToDestination.angle(playerDirection);
                    })
                    .min().orElse(-1);

            final boolean exempt = (Bukkit.getPlayer(playerData.getUuid()).getLocation().toVector().setY(0).distance(target.getLocation().toVector().setY(0)) - .42) < 1.8;
            final boolean invalid = angle > 0.6;

            if (invalid && !exempt) {
                if (incrementBuffer() > 4) {
                    flag(playerData, String.format("A: %s", angle), String.format("B: %s", buffer));
                }
            } else {
                decrementBuffer();
            }
        }
    }

    public int getPing(Player player) {
        int ping = 0;
        try {
            Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
            ping = (int) entityPlayer.getClass().getField("ping").get(entityPlayer);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        return ping;
    }

    public int msToTicks(final double time) {
        return (int) Math.round(time / 50.0);
    }
}
