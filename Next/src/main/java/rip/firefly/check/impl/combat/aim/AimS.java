package rip.firefly.check.impl.combat.aim;

import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.MovementCheck;
import rip.firefly.check.types.PacketCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.packet.tinyprotocol.api.NMSObject;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInFlyingPacket;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInUseEntityPacket;
import rip.firefly.util.location.PlayerLocation;
import rip.firefly.util.math.MathUtil;
import rip.firefly.util.update.MovementUpdate;

@CheckData(name = "Aim", subType = "S", description = "Checks If The Player's Rotation GCD Is Too Low", type = CheckType.COMBAT, threshold = 5)
public class AimS extends PacketCheck {

    private static final double MULTIPLIER = Math.pow(2.0D, 24.0D);
    private float lastDeltaPitch = Float.NaN;
    private int lastAttackTicks = 6;
    private int total;
    private int bad;
    private float totalDeltaYaw;

    public AimS(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(PlayerData playerData, NMSObject wpacket) {
        if (wpacket instanceof WrappedInUseEntityPacket) {
            WrappedInUseEntityPacket packet = (WrappedInUseEntityPacket)wpacket;
            if (packet.getAction() == WrappedInUseEntityPacket.EnumEntityUseAction.ATTACK) {
                lastAttackTicks = 0;
            }
        } else if (wpacket instanceof WrappedInFlyingPacket) {
            ++lastAttackTicks;
            WrappedInFlyingPacket packet = (WrappedInFlyingPacket)wpacket;
            if (packet.isLook()) {
                float deltaYaw = Math.abs(playerData.getTo().getYaw() - playerData.getFrom().getYaw());
                float deltaPitch = Math.abs(playerData.getTo().getPitch() - playerData.getFrom().getPitch());
                if (!Float.isNaN(lastDeltaPitch) && deltaPitch != 0.0F && deltaPitch <= 10.0F && Math.abs(playerData.getFrom().getPitch()) != 90.0F && Math.abs(playerData.getTo().getPitch()) != 90.0F && lastAttackTicks <= 5) {
                    long one = (long)((double)deltaPitch * MULTIPLIER);
                    long two = (long)((double)lastDeltaPitch * MULTIPLIER);
                    long gcd = gcd(one, two);
                    if (gcd <= 131072L) {
                        ++bad;
                    }

                    totalDeltaYaw += deltaYaw;
                    if (++total == 250) {
                        if (bad >= 100 && totalDeltaYaw >= 180.0F) {
                            flag(playerData, new String[]{"B: " + bad + "/250 Y: " + totalDeltaYaw});
                        }

                        total = 0;
                        bad = 0;
                        totalDeltaYaw = 0.0F;
                    }
                }

                lastDeltaPitch = deltaPitch;
            }

        }
    }

    private long gcd(long one, long two) {
        return two <= 16384L ? one : gcd(two, one % two);
    }
}