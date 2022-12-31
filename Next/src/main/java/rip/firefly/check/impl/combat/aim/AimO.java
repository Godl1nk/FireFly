package rip.firefly.check.impl.combat.aim;

import org.bukkit.Bukkit;
import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.PacketCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.packet.tinyprotocol.api.NMSObject;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInFlyingPacket;

import java.util.concurrent.TimeUnit;

@CheckData(name = "Aim", subType = "O", description = "Checks If The Player's Rotation Acceleration Is Too High", type = CheckType.COMBAT, threshold = 7, enterprise = true)
public class AimO extends PacketCheck {

    private double threshold;

    private float lastPitchDifference;
    private float lastYawDifference;

    private final double offset = Math.pow(2.0, 24.0);

    public AimO(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(PlayerData playerData, NMSObject packet) {
        if (packet instanceof WrappedInFlyingPacket && ((WrappedInFlyingPacket) packet).isLook()) {
            if (hasExpired(playerData.getLastAttack(), 2L) || Bukkit.getPlayer(playerData.getUuid()).isSneaking()) {
                return;
            }

            if(playerData.isCinematic()) {
                return;
            }

            float pitchDifference = Math.abs(((WrappedInFlyingPacket) packet).getPitch()
                    - playerData.getFrom().getPitch());

            float yawDifference = Math.abs(((WrappedInFlyingPacket) packet).getYaw()
                    - playerData.getFrom().getYaw());

            float yawAccel = Math.abs(pitchDifference - lastPitchDifference);
            float pitchAccel = Math.abs(yawDifference - lastYawDifference);

            long gcd = gcd((long) (pitchDifference * offset), (long) (lastPitchDifference * offset));



            if (yawDifference > 2.0F && yawAccel > 1.0F && pitchAccel > 0.0F && pitchDifference > 0.009f) {

                if (gcd < 131072L && pitchAccel < 6.5) {
                    threshold += 0.89;

                    if (threshold > 12.5) {
                        flag(playerData, String.format("YD: %s", yawDifference), String.format("YA: %s", yawAccel), String.format("PD: %s", pitchDifference), String.format("PA: %s", pitchAccel));
                    }
                } else {
                    threshold -= Math.min(threshold, 0.25);
                }
            }

            lastYawDifference = yawDifference;
            lastPitchDifference = pitchDifference;
        }
    }

    public static boolean hasExpired ( long timestamp, long seconds){
        return System.currentTimeMillis() - timestamp > TimeUnit.SECONDS.toMillis(seconds);
    }

    public static long gcd(long current, long last) {
        if (last <= 16384) return current;
        return gcd(last, current % last);
    }
}