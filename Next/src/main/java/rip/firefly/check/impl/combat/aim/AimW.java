package rip.firefly.check.impl.combat.aim;

import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.MovementCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.util.location.PlayerLocation;
import rip.firefly.util.math.MathUtil;
import rip.firefly.util.update.MovementUpdate;

@CheckData(name = "Aim", subType = "W", description = "Checks If The Player's Rotation Change Is Invalid", type = CheckType.COMBAT, threshold = 5)
public class AimW extends MovementCheck {

    public AimW(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(PlayerData playerData, MovementUpdate update) {
        if(playerData.isCinematic() || !update.isRotation()) {
            return;
        }
        float deltaA = playerData.movement.deltaPitch % 0.1f, deltaB = playerData.movement.deltaPitch % 0.05f;
        if(playerData.movement.deltaPitch > 0 && Math.abs(playerData.movement.deltaPitch) < 100
                && (deltaA == 0 || deltaB == 0 || playerData.movement.deltaPitch % 1f == 0)) {
            if(increaseBuffer(2) > 10) {
                flag(playerData, new String[]{"D: " + playerData.movement.deltaPitch, "A: " + deltaA, "B: " + deltaB});
            }
        } else decrementBuffer();
    }
}