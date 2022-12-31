package rip.firefly.check.impl.combat.aim.sub;

import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.MovementCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.packet.tinyprotocol.api.ProtocolVersion;
import rip.firefly.util.math.MathUtil;
import rip.firefly.util.update.MovementUpdate;

@CheckData(name = "Aim", subType = "AB", description = "Checks If The Player's Rotation GCD Is Too Consistent", type = CheckType.COMBAT, threshold = 10)
public class AimAB extends MovementCheck {
    private float lastDeltaPitch = 0.0F;
    private boolean applied = false;
    private int rotations = 0;
    private final long[] grid = new long[10];

    public AimAB(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(PlayerData playerData, MovementUpdate update) {
        if(!update.isRotation()) return;
        float deltaYaw = playerData.getMovement().deltaYaw;
        float deltaPitch = playerData.getMovement().deltaPitch;
        boolean cinematic = playerData.isCinematic();
        boolean attacking = System.currentTimeMillis() - playerData.getLastAttack() < 2000L;
        long deviation = this.getDeviation(deltaPitch);
        ++this.rotations;
        this.grid[this.rotations % this.grid.length] = deviation;
        if ((double)deltaYaw > 0.0D && (double)deltaPitch > 0.0D && deltaYaw < 30.0F && deltaPitch < 30.0F && /*!cinematic &&*/ attacking) {
            boolean reached = this.rotations > this.grid.length;
            if (reached) {
                double deviationMax = 0.0D;
                long[] var11 = this.grid;
                int var12 = var11.length;

                for(int var13 = 0; var13 < var12; ++var13) {
                    double l = (double)var11[var13];
                    if (deviation != 0L && l != 0.0D) {
                        deviationMax = Math.max(Math.max(l, (double)deviation) % Math.min(l, (double)deviation), deviationMax);
                    }
                }

                if (deviationMax > 0.0D && (double)deviation > 0.0D) {
                    flag(playerData, new String[]{"D:" + deviation, "DM: " + deviationMax});
                    this.applied = false;
                }
            }
        }

        this.lastDeltaPitch = deltaPitch;
    }

    private long getDeviation(float deltaPitch) {
        long expandedPitch = (long)((double)deltaPitch * MathUtil.EXPANDER);
        long previousExpandedPitch = (long)((double)this.lastDeltaPitch * MathUtil.EXPANDER);
        long result = this.applied ? MathUtil.getGcd(expandedPitch, previousExpandedPitch) : 0L;
        if (this.applied) {
            this.applied = false;
            return result;
        } else {
            return 0L;
        }
    }
}