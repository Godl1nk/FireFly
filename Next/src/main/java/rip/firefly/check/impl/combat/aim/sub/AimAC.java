package rip.firefly.check.impl.combat.aim.sub;

import org.bukkit.Bukkit;
import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.MovementCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.util.math.MathUtil;
import rip.firefly.util.update.MovementUpdate;

@CheckData(name = "Aim", subType = "AC", description = "Checks If The Player's Rotations Snap", type = CheckType.COMBAT, threshold = 10)
public class AimAC extends MovementCheck {
    private float lastDeltaYaw;
    private float lastLastDeltaYaw;

    public AimAC(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(PlayerData playerData, MovementUpdate update) {
        if(!update.isRotation()) return;
        boolean exempt = (System.currentTimeMillis() - playerData.getLastJoin() < 2000L) || (playerData.getTeleportTicks() < 10) || Bukkit.getPlayer(playerData.getUuid()).isInsideVehicle();
        float deltaYaw = playerData.getMovement().getDeltaYaw();
        if (deltaYaw < 5.0F && this.lastDeltaYaw > 30.0F && this.lastLastDeltaYaw < 5.0F) {
            double low = (double)((deltaYaw + this.lastLastDeltaYaw) / 2.0F);
            double high = (double)this.lastDeltaYaw;
            if (this.incrementBuffer() > 5.0D && !exempt) {
                flag(playerData, new String[]{"DY: " + deltaYaw, "LDY:" + this.lastDeltaYaw});
            } else {
                this.decreaseBuffer(0.1D);
            }
        }

        this.lastLastDeltaYaw = this.lastDeltaYaw;
        this.lastDeltaYaw = deltaYaw;
    }
}