package rip.firefly.check.impl.combat.aim;

import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.MovementCheck;
import rip.firefly.check.types.PacketCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.packet.tinyprotocol.api.NMSObject;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInFlyingPacket;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInUseEntityPacket;
import rip.firefly.util.math.MathUtil;
import rip.firefly.util.update.MovementUpdate;

@CheckData(name = "Aim", subType = "U", description = "Checks If The Player's Rotation Change Is Invalid", type = CheckType.COMBAT, threshold = 5)
public class AimU  extends MovementCheck {

    private double multiplier;
    private float previous;
    private double vl;
    private double streak;

    public AimU(PlayerData data) {
        super(data);
        this.multiplier = Math.pow(2.0, 24.0);
    }

    @Override
    public void handle(PlayerData playerData, MovementUpdate update) {

        if(playerData.isCinematic() || !update.isRotation()) {
            return;
        }

        if (System.currentTimeMillis() - this.playerData.getLastAttack() >= 2000L) {
            this.vl = 0.0;
            this.streak = 0.0;
            return;
        }
        if (playerData.getTeleportTicks() > 0) {
            this.vl = 0.0;
            return;
        }
        final float pitchChange = MathUtil.getDistanceBetweenAngles(update.getTo().getPitch(), update.getFrom().getPitch());
        final long a = (long)(pitchChange * this.multiplier);
        final long b = (long)(this.previous * this.multiplier);
        final long gcd = this.gcd(a, b);
        final float magicVal = pitchChange * 100.0f / this.previous;
        if (magicVal > 60.0f) {
            this.vl = Math.max(0.0, this.vl - 1.0);
            this.streak = Math.max(0.0, this.streak - 0.125);
        }
        if (pitchChange > 0.5 && pitchChange <= 20.0f && gcd < 131072L) {
            final double vl = this.vl + 1.0;
            this.vl = vl;
            if (vl > 1.0) {
                ++this.streak;
            }
            if (this.streak > 6.0) {
                flag(playerData, new String[]{"GCD: " + gcd, "PC: " + pitchChange});
            }
        }
        else {
            this.vl = Math.max(0.0, this.vl - 1.0);
        }
        this.previous = pitchChange;
    }

    private long gcd(final long a, final long b) {
        if (b <= 16384L) {
            return a;
        }
        return this.gcd(b, a % b);
    }
}