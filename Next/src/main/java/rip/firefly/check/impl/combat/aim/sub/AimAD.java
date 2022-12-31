package rip.firefly.check.impl.combat.aim.sub;

import org.bukkit.Bukkit;
import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.MovementCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.packet.tinyprotocol.api.ProtocolVersion;
import rip.firefly.util.math.MathUtil;
import rip.firefly.util.update.MovementUpdate;

@CheckData(name = "Aim", subType = "AD", description = "Checks If The Player's Rotations Snap", type = CheckType.COMBAT, threshold = 10)
public class AimAD extends MovementCheck {
    private float lastDeltaYaw;
    private float lastDeltaPitch;

    public AimAD(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(PlayerData playerData, MovementUpdate update) {
        if(!update.isRotation()) return;


//        if (playerData.isCinematic()) {
//            return;
//        }

        if (playerData.getPlayerVersion().isOrAbove(ProtocolVersion.V1_9)) {
            return;
        }

        float deltaYaw = (update.getTo().getYaw() - update.getFrom().getYaw()) % 360.0F;
        float deltaPitch = (update.getTo().getPitch() - update.getFrom().getPitch());
        double divisorYaw = (double) MathUtil.getGcd((long)((double)deltaYaw * MathUtil.EXPANDER), (long)((double)this.lastDeltaYaw * MathUtil.EXPANDER));
        double divisorPitch = (double)MathUtil.getGcd((long)((double)deltaPitch * MathUtil.EXPANDER), (long)((double)this.lastDeltaPitch * MathUtil.EXPANDER));
        double constantYaw = divisorYaw / MathUtil.EXPANDER;
        double constantPitch = divisorPitch / MathUtil.EXPANDER;
        double currentX = (double)deltaYaw / constantYaw;
        double currentY = (double)deltaPitch / constantPitch;
        double previousX = (double)this.lastDeltaYaw / constantYaw;
        double previousY = (double)this.lastDeltaPitch / constantPitch;
        if (playerData.isCinematic()) {
            return;
        }

        if ((double)deltaYaw > 0.0D && (double)deltaPitch > 0.0D && deltaYaw < 20.0F && deltaPitch < 20.0F) {
            double moduloX = currentX % previousX;
            double moduloY = currentY % previousY;
            double floorModuloX = Math.abs(Math.floor(moduloX) - moduloX);
            double floorModuloY = Math.abs(Math.floor(moduloY) - moduloY);
            boolean invalidX = moduloX > 90.0D && floorModuloX > 0.1D;
            boolean invalidY = moduloY > 90.0D && floorModuloY > 0.1D;
            if (invalidX && invalidY) {
                if (++this.buffer > 8.0D) {
                    flag(playerData, new String[]{"MX: " + moduloX, "MY: " + moduloY});
                }
            } else {
                this.buffer -= this.buffer > 0.0D ? 1.0D : 0.0D;
            }
        }

        this.lastDeltaYaw = deltaYaw;
        this.lastDeltaPitch = deltaPitch;
    }
}