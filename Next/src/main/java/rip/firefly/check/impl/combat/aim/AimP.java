package rip.firefly.check.impl.combat.aim;

import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.PacketCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.packet.tinyprotocol.api.NMSObject;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInFlyingPacket;
import rip.firefly.util.math.MathUtil;

@CheckData(name = "Aim", subType = "P", description = "Checks If The Player's Rotation GCD Is Lower Than Possible", type = CheckType.COMBAT, threshold = 10)
public class AimP extends PacketCheck {

    private float lastDeltaYaw = 0.0f, lastDeltaPitch = 0.0f;

    private static final double MODULO_THRESHOLD = 90F;
    private static final double LINEAR_THRESHOLD = 0.1F;

    public AimP(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(PlayerData playerData, NMSObject packet) {
        if (packet instanceof WrappedInFlyingPacket && ((WrappedInFlyingPacket) packet).isLook()) {

            if(playerData.isCinematic()) {
                return;
            }

            final int now = playerData.getTicks();

            // Get the deltas from the rotation update
            final float deltaYaw = playerData.movement.deltaYaw;
            final float deltaPitch = playerData.movement.deltaPitch;

            // Grab the gcd using an expander.
            final double divisorYaw = getGcd((long) (deltaYaw * MathUtil.EXPANDER), (long) (lastDeltaYaw * MathUtil.EXPANDER));
            final double divisorPitch = getGcd((long) (deltaPitch * MathUtil.EXPANDER), (long) (lastDeltaPitch * MathUtil.EXPANDER));

            // Get the constant for both rotation updates by dividing by the expander
            final double constantYaw = divisorYaw / MathUtil.EXPANDER;
            final double constantPitch = divisorPitch / MathUtil.EXPANDER;

            // Get the estimated mouse delta from the constant
            final double currentX = deltaYaw / constantYaw;
            final double currentY = deltaPitch / constantPitch;

            // Get the estimated mouse delta from the old rotations using the new constant
            final double previousX = lastDeltaYaw / constantYaw;
            final double previousY = lastDeltaPitch / constantPitch;

            // Make sure the player is attacking or placing to filter out the check
            final boolean action = now - playerData.getLastAttack() < 30
                    || now - playerData.getLastPlace() < 30;

            // Make sure the rotation is not very large and not equal to zero and get the modulo of the xys
            if (deltaYaw > 0.0 && deltaPitch > 0.0 && deltaYaw < 20.f && deltaPitch < 20.f && action) {
                final double moduloX = currentX % previousX;
                final double moduloY = currentY % previousY;

                // Get the floor delta of the the modulos
                final double floorModuloX = Math.abs(Math.floor(moduloX) - moduloX);
                final double floorModuloY = Math.abs(Math.floor(moduloY) - moduloY);

                // Impossible to have a different constant in two rotations
                final boolean invalidX = moduloX > MODULO_THRESHOLD && floorModuloX > LINEAR_THRESHOLD;
                final boolean invalidY = moduloY > MODULO_THRESHOLD && floorModuloY > LINEAR_THRESHOLD;

                if (invalidX && invalidY) {
                    buffer = Math.min(buffer + 1, 200);

                    if (buffer > 6) flag(playerData, String.format("MX: %s", moduloX), String.format("MY: %s", moduloY));
                } else {
                    buffer = 0;
                }
            }

            this.lastDeltaYaw = deltaYaw;
            this.lastDeltaPitch = deltaPitch;
        }
    }

    /**
     *
     * @param current - The current value
     * @param previous - The previous value
     * @return - The GCD of those two values
     */
    public long getGcd(final long current, final long previous) {
        return (previous <= 16384L) ? current : getGcd(previous, current % previous);
    }
}