package rip.firefly.check.impl.movement.velocity;

import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.MovementCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.manager.VelocityManager;
import rip.firefly.util.location.PlayerLocation;
import rip.firefly.util.update.MovementUpdate;

@CheckData(name = "Velocity", subType = "A", description = "Checks For Invalid Velocity", threshold = 5, experimental = true, type = CheckType.MOVEMENT)
public class VelocityA extends MovementCheck {
    public VelocityA(PlayerData data) {
        super(data);
    }

    private int buffer;

    @Override
    public void handle(PlayerData playerData, MovementUpdate movementUpdate) {
        final PlayerLocation from = movementUpdate.getFrom();
        final PlayerLocation to = movementUpdate.getTo();

        final double deltaY = to.getY() - from.getY();

        if (from.getY() % 1.0 == 0.0 && to.getY() % 1.0 > 0.0 && deltaY > 0.0 && deltaY < 0.41999998688697815D && !playerData.isBelowBlocks()) {
            final double velocityY = playerData.getVelocityManager().getVelocities().stream().mapToDouble(VelocityManager.VelocitySnapshot::getVertical).min().orElse(0.0);

            if (velocityY > 0.0) {
                final double ratio = deltaY / velocityY;


                if (ratio < 0.99) {
                    if (++buffer > 3) {
                        flag(playerData, String.format("R: %s", ratio), String.format("VY: %s", velocityY), String.format("DY: %s", deltaY));
                    }
                } else {
                    buffer = 0;
                }
            }
        }
    }
}
