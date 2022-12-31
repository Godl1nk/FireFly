package rip.firefly.check.impl.combat.aim;

import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.MovementCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.util.update.MovementUpdate;

@CheckData(name = "Aim", subType = "T", description = "Checks If The Player's Rotation Change Is Invalid", type = CheckType.COMBAT, threshold = 5)
public class AimT extends MovementCheck {

    private float lastYawRate;
    private float lastPitchRate;

    public AimT(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(PlayerData playerData, MovementUpdate update) {
        if(playerData.isCinematic() || !update.isRotation()) {
            return;
        }
        final double yaw = Math.abs(update.getTo().getYaw() - update.getFrom().getYaw()) % 360.0f;
        final double offset = yaw % 3.141592653589793;
        final double value = yaw % offset;
        final double magic = yaw - (offset + value);
        if (yaw > 1400.0 && magic > 1000.0 && value < 3.0 && offset < 3.0 && System.currentTimeMillis() - playerData.getLastAttack() < 250L) {
            flag(playerData, new String[]{"Y: " + yaw, "V: " + value, "M: " + magic, "O: " + offset});
        }
    }
}