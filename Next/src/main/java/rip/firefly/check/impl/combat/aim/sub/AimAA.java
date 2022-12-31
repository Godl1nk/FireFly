package rip.firefly.check.impl.combat.aim.sub;

import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.MovementCheck;
import rip.firefly.check.types.PacketCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.packet.tinyprotocol.api.NMSObject;
import rip.firefly.packet.tinyprotocol.api.ProtocolVersion;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInFlyingPacket;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInUseEntityPacket;
import rip.firefly.util.math.MathUtil;
import rip.firefly.util.update.MovementUpdate;

@CheckData(name = "Aim", subType = "AA", description = "Checks If The Player's Rotation Divisor Is Incorrect", type = CheckType.COMBAT, threshold = 10)
public class AimAA extends PacketCheck {


    private double multiplier = Math.pow(2.0, 24.0);
    private float previous;
    private double streak;
    public AimAA(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(PlayerData user, NMSObject packet) {

        if(packet instanceof WrappedInFlyingPacket && ((WrappedInFlyingPacket)packet).isLook()) {
            if (playerData.getTeleportTicks() > 0) {
                resetBuffer();
                return;
            }
            final float pitchChange = MathUtil.getDistanceBetweenAngles(user.getTo().getPitch(), user.getFrom().getPitch());
            final long a = (long)(pitchChange * multiplier);
            final long b = (long)(previous * multiplier);
            final long gcd = gcd(a, b);
            final float magicVal = pitchChange * 100.0f / previous;
            if (magicVal > 60.0f) {
                decrementBuffer();
                streak = Math.max(0.0, streak - 0.125);
            }
            if (pitchChange > 0.5 && pitchChange <= 20.0f && gcd < 131072L) {
                if (incrementBuffer() > 1.0) {
                    ++streak;
                }
                if (streak > 6.0) {
                    flag(user, new String[]{"GCD: " + gcd, "B: " + getBuffer(), "PC: " + pitchChange});
                }
            }
            else {
                decrementBuffer();
            }
            previous = pitchChange;
        }


    }

    private long gcd(final long a, final long b) {
        if (b <= 16384L) {
            return a;
        }
        return gcd(b, a % b);
    }
}