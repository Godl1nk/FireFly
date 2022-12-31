package rip.firefly.check.impl.combat.aim;

import org.bukkit.Bukkit;
import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.MovementCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.util.math.MathUtil;
import rip.firefly.util.update.MovementUpdate;

@CheckData(name = "Aim", subType = "N", description = "Checks For Invalid Rotations That Don't Follow GCD", type = CheckType.COMBAT, threshold = 6)
public class AimN extends MovementCheck {

    private double multiplier;
    private float previous;
    private double vl;
    private double streak;

    public AimN(PlayerData data) {
        super(data);
        this.multiplier = Math.pow(2.0, 24.0);
    }

    @Override
    public void handle(PlayerData playerData, MovementUpdate update) {
        if(update.isRotation()) {
            if (System.currentTimeMillis() - this.playerData.getLastAttack() >= 2000L) {
                resetBuffer();
                this.streak = 0.0;
                return;
            }
            if (playerData.getTeleportTicks() > 0 || Bukkit.getPlayer(playerData.getUuid()).isInsideVehicle()) {
                resetBuffer();
                return;
            }
            final float pitchChange = MathUtil.getDistanceBetweenAngles(update.getTo().getPitch(), update.getFrom().getPitch());
            final long a = (long) (pitchChange * this.multiplier);
            final long b = (long) (this.previous * this.multiplier);
            final long gcd = this.gcd(a, b);
            final float magicVal = pitchChange * 100.0f / this.previous;
            if (magicVal > 60.0f) {
                decrementBuffer();
                this.streak = Math.max(0.0, this.streak - 0.125);
            }
            if (pitchChange > 0.5 && pitchChange <= 20.0f && gcd < 131072L) {
                incrementBuffer();
                if (getBuffer() > 1.0) {
                    ++this.streak;
                }
                if (this.streak > 6.0) {
                    flag(playerData, String.format("GCD: %s", gcd), String.format("PC: %s", pitchChange));
                }
            } else {
                this.vl = Math.max(0.0, this.vl - 1.0);
            }
            this.previous = pitchChange;
        }
    }

    private long gcd(final long a, final long b) {
        if (b <= 16384L) {
            return a;
        }
        return this.gcd(b, a % b);
    }
}
