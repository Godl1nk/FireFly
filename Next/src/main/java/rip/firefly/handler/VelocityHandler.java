package rip.firefly.handler;

import org.bukkit.util.Vector;
import rip.firefly.data.PlayerData;
import rip.firefly.util.location.PlayerLocation;
import rip.firefly.util.update.MovementUpdate;

public class VelocityHandler {

    public static void handle(PlayerData playerData, MovementUpdate movementUpdate) {
        final PlayerLocation from = movementUpdate.getFrom();
        final PlayerLocation to = movementUpdate.getTo();

        final double deltaX = to.getX() - from.getX();
        final double deltaZ = to.getZ() - from.getZ();

        final boolean velocity = playerData.isVelocity();

        if (velocity) {
            final boolean attacked = playerData.isAttacking();

            final double velocityX = playerData.getVelocityX();
            final double velocityZ = playerData.getVelocityZ();

            final Vector movementVector = new Vector(deltaX, 0, deltaZ);
            final Vector velocityVector = new Vector(velocityX, 0, velocityZ);

            if (attacked) {
                velocityVector.multiply(0.6);
            }

            final double vectorSubtracted = velocityVector.subtract(movementVector).length();

            if (vectorSubtracted > 0.4) {

            } else {

            }

            playerData.setVelocity(false);
        }
    }
}
