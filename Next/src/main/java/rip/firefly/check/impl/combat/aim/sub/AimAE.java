package rip.firefly.check.impl.combat.aim.sub;

import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.MovementCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.packet.tinyprotocol.api.ProtocolVersion;
import rip.firefly.util.math.MathUtil;
import rip.firefly.util.update.MovementUpdate;

@CheckData(name = "Aim", subType = "AE", description = "Checks If The Player's Rotation Change Is Too Small", type = CheckType.COMBAT, threshold = 10)
public class AimAE extends MovementCheck {
    private float lastDeltaYaw;
    private float lastDeltaPitch;

    public AimAE(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(PlayerData playerData, MovementUpdate update) {
        if(!update.isRotation()) return;

        float yaw = update.getTo().getYaw();
        float pitch = update.getTo().getPitch();

        float deltaYaw = playerData.getMovement().getDeltaYaw();

        float deltaPitch = playerData.getMovement().getDeltaPitch();
        float lastDeltaPitch = playerData.getMovement().getLastDeltaPitch();
        double divisor = MathUtil.getGcd(deltaPitch, lastDeltaPitch);

        if (divisor < 0.0078125F) return;

        double deltaX = deltaYaw / divisor;
        double deltaY = deltaPitch / divisor;

        boolean properX = Math.abs(Math.round(deltaX) - deltaX) < 0.0001D;
        boolean properY = Math.abs(Math.round(deltaY) - deltaY) < 0.0001D;

        if (!properX || !properY || (System.currentTimeMillis() - playerData.getLastAttack()) > 2000L) return;

        double diffX = Math.abs(yaw - (yaw - (yaw % divisor)));
        double diffY = Math.abs(pitch - (pitch - (pitch % divisor)));

        if (diffX < 1e-4 && diffY < 1e-4 && (System.currentTimeMillis() - playerData.getLastTeleport() > 1000L)) {
            if (incrementBuffer() > 5) {
                flag(playerData, new String[]{"X: " + diffX, "Y: " + diffY});
            }
        } else {
            decreaseBuffer(0.25);
        }
    }
}