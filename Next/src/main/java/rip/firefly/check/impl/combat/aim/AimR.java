package rip.firefly.check.impl.combat.aim;

import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.MovementCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.util.math.MathUtil;
import rip.firefly.util.update.MovementUpdate;

@CheckData(name = "Aim", subType = "R", description = "Checks If The Player's Rotation Change Isn't Possible", type = CheckType.COMBAT, threshold = 5)
public class AimR extends MovementCheck {

    private float lastYawRate;
    private float lastPitchRate;

    public AimR(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(PlayerData playerData, MovementUpdate update) {
        if(playerData.isCinematic() || !update.isRotation()) {
            return;
        }
        final float diffPitch = MathUtil.getDistanceBetweenAngles(update.getTo().getPitch(), update.getFrom().getPitch());
        final float diffYaw = MathUtil.getDistanceBetweenAngles(update.getTo().getYaw(), update.getFrom().getYaw());
        final float diffPitchRate = Math.abs(lastPitchRate - diffPitch);
        final float diffYawRate = Math.abs(lastYawRate - diffYaw);
        final float diffPitchRatePitch = Math.abs(diffPitchRate - diffPitch);
        final float diffYawRateYaw = Math.abs(diffYawRate - diffYaw);
        if (diffPitch < 0.009 && diffPitch > 0.001 && diffPitchRate > 1.0 && diffYawRate > 1.0 && diffYaw > 3.0 && lastYawRate > 1.5 && (diffPitchRatePitch > 1.0f || diffYawRateYaw > 1.0f)) {
            flag(playerData, new String[]{"DPR: " + diffPitchRate, "DYR: " + diffYawRate, "LPR: " + this.lastPitchRate, "LYR: " + this.lastYawRate, "DP: " + diffPitch, "DY: " + diffYaw});
        }
        this.lastPitchRate = diffPitch;
        this.lastYawRate = diffYaw;
    }

    private long gcd(final long a, final long b) {
        if (b <= 16384L) {
            return a;
        }
        return this.gcd(b, a % b);
    }
}