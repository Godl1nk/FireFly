package rip.firefly.check.impl.movement.scaffold;

import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.MovementCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.util.location.PlayerLocation;
import rip.firefly.util.math.MathUtil;
import rip.firefly.util.update.MovementUpdate;

@CheckData(name = "Scaffold", subType = "A", description = "Checks For Invalid Rotations", threshold = 10, type = CheckType.MOVEMENT)
public class ScaffoldA extends MovementCheck {

    public ScaffoldA(PlayerData data) {
        super(data);
    }

    private double multiplier = Math.pow(2.0, 24.0);
    private float previous;
    private double streak;

    @Override
    public void handle(PlayerData playerData, MovementUpdate movementUpdate) {
        PlayerLocation to = movementUpdate.getTo();
        PlayerLocation from = movementUpdate.getFrom();
        if (System.currentTimeMillis() - playerData.getLastPlace() > 1000L) {
            resetBuffer();
            return;
        }
        float pitchChange = MathUtil.getDistanceBetweenAngles(to.getPitch(), from.getPitch());
        long a = (long)((double)pitchChange * this.multiplier);
        long b = (long)((double)this.previous * this.multiplier);
        long gcd = this.gcd(a, b);
        float magicVal = pitchChange * 100.0f / this.previous;
        if (magicVal > 24.0f) {
            decrementBuffer();
        }
        if ((double)pitchChange >= 0.05 && pitchChange <= 20.0f && gcd < 131072L) {
            incrementBuffer();
            if (this.streak > 6.0) {
                flag(playerData, String.format("GCD: %s", gcd), String.format("S: %s", streak), String.format("PC: %s", pitchChange), String.format("MV: %s", magicVal));
            }
        } else {
            decrementBuffer();
            this.streak = Math.max(0.0, this.streak - 0.25);
        }
        this.previous = pitchChange;
    }

    private long gcd(long a, long b) {
        if (b <= 16384L) {
            return a;
        }
        return this.gcd(b, a % b);
    }

}
