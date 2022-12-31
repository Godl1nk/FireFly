package rip.firefly.check.impl.combat.aim;

import org.bukkit.Bukkit;
import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.PacketCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.packet.tinyprotocol.api.NMSObject;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInFlyingPacket;
import rip.firefly.util.location.PlayerLocation;

import java.util.concurrent.TimeUnit;

@CheckData(name = "Aim", subType = "K", description = "Checks For Badly Rounded Yaw Values", type = CheckType.COMBAT, threshold = 7, enterprise = true)
public class AimK extends PacketCheck {

    private float suspiciousYaw;

    public AimK(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(PlayerData playerData, NMSObject packet) {
        if (packet instanceof WrappedInFlyingPacket && ((WrappedInFlyingPacket) packet).isLook()) {
            if (hasExpired(playerData.getLastAttack(), 20L) || Bukkit.getPlayer(playerData.getUuid()).isSneaking()) {
                return;
            }

            if(playerData.isCinematic()) {
                return;
            }

            PlayerLocation to = playerData.getTo();
            PlayerLocation from = playerData.getFrom();

            if (playerData.getLastAttack() > 20 * 60)
                return;

            float yawChange = Math.abs(to.getYaw() - from.getYaw());

            if (yawChange > 1F && Math.round(yawChange) == yawChange && yawChange % 1.5F != 0F) {
                if (yawChange == suspiciousYaw) {
                    flag(playerData, String.format("SY: %s", suspiciousYaw), String.format("YC: %s", yawChange));
                }

                suspiciousYaw = Math.round(yawChange);
            } else {
                suspiciousYaw = 0F;
            }
        }
    }

    public static boolean hasExpired ( long timestamp, long seconds){
        return System.currentTimeMillis() - timestamp > TimeUnit.SECONDS.toMillis(seconds);
    }
}