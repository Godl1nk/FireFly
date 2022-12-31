package rip.firefly.check.impl.movement.motion;

import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.MovementCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.util.location.PlayerLocation;
import rip.firefly.util.update.MovementUpdate;

@CheckData(name = "Motion", subType = "A", description = "Checks For Invalid Motion", threshold = 10, experimental = true, type = CheckType.MOVEMENT)
public class MotionA extends MovementCheck {

    private double lastDeltaY;
    private double buffer;
    private int airTicks;

    public MotionA(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(PlayerData playerData, MovementUpdate movementUpdate) {
        final PlayerLocation from = movementUpdate.getFrom();
        final PlayerLocation to = movementUpdate.getTo();

        final double deltaY = to.getY() - from.getY();

        if (playerData.getVelocityManager().getMaxVertical() > 0.0) {
            return;
        }

        final boolean touchingAir = playerData.isTouchingAir();

        if (touchingAir) {
            ++airTicks;

            if (airTicks > 9) {
                final double estimation = (lastDeltaY * 0.9800000190734863) - 0.08;

                if (Math.abs(deltaY + 0.0980000019) < 0.005) {
                    buffer = 0.0D;
                    return;
                }

                if (Math.abs(estimation - deltaY) > 0.002) {
                    buffer += 1.5;

                    if (buffer > 5) {
                        flag(playerData, String.format("E: %s", estimation), String.format("AT: %s", airTicks), String.format("LDY: %s", lastDeltaY), String.format("B: %s", buffer));
                    }
                } else {
                    buffer = Math.max(0, buffer - 1.25);
                }
            }
        } else {
            airTicks = 0;

            buffer = Math.max(0, buffer - 10D);
        }

        this.lastDeltaY = deltaY;
    }

}
