package rip.firefly.check.impl.combat.aim.sub;

import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.MovementCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.util.math.MathUtil;
import rip.firefly.util.update.MovementUpdate;

@CheckData(name = "Aim", subType = "AF", description = "Checks If The Player's Rotation Divisor Is Incorrect", type = CheckType.COMBAT, threshold = 10)
public class AimAF extends MovementCheck {


    private double offset = Math.pow(2.0, 24.0);
    private double lastPitchDiff;

    public AimAF(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(PlayerData user, MovementUpdate update) {
        if(!update.isRotation()) return;


    //    boolean work = (System.currentTimeMillis() - user.getLastAttack() < 1000L) || (System.currentTimeMillis() - user.getLastPlace()) < 155L;

        boolean work = true;
  //      if (getBuffer() > 0) decreaseBuffer(7);

        float pitchDifference = Math.abs(update.getFrom().getPitch() - update.getTo().getPitch());
        double gcd = MathUtil.gcd((long) (pitchDifference * offset), (long) (lastPitchDiff * offset));

        if (((update.getTo().getYaw() != update.getFrom().getYaw()) && (update.getTo().getPitch() != update.getFrom().getPitch()))) {
            if (Math.abs(update.getTo().getPitch() - update.getFrom().getPitch()) > 0.0 && Math.abs(update.getTo().getPitch()) != 90.0f) {
                if (gcd < 131072L && work) {
                    if (getBuffer() < 10) incrementBuffer();
                } else {
                    if (getBuffer() > 0) decrementBuffer();
                }
            }
        }

        if (getBuffer() > 9) flag(user, new String[]{"GCD: " + gcd, "B: " + getBuffer()});

        lastPitchDiff = pitchDifference;
    }
}