package rip.firefly.check.impl.combat.aim;

import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.MovementCheck;
import rip.firefly.check.types.PacketCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.packet.tinyprotocol.api.NMSObject;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInFlyingPacket;
import rip.firefly.util.location.PlayerLocation;
import rip.firefly.util.math.MathUtil;
import rip.firefly.util.update.MovementUpdate;

import java.util.ArrayList;
import java.util.List;

@CheckData(name = "Aim", subType = "J", description = "Checks If The Player's Rotation GCD Is Lower Than Possible", type = CheckType.COMBAT, threshold = 7)
public class AimJ extends MovementCheck {


    private int verbose;
    private double lastDeltaPitch;

    public AimJ(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(PlayerData playerData, MovementUpdate update) {
        if(playerData.isCinematic() || !update.isRotation()) {
            return;
        }
        final double pitch = Math.abs(update.getTo().getPitch() - update.getFrom().getPitch());
        final double deltaPitch = lastDeltaPitch;
        lastDeltaPitch = pitch;
        final double pitchAcceleration = Math.abs(pitch - deltaPitch);
        final double offset = Math.pow(2.0, 24.0);
        final double gcd = (double)this.gcd((long)(pitch * offset), (long)(deltaPitch * offset));
        final double simple = gcd / offset;
        final double magic = pitch % simple;
        if (pitch > 0.0 && magic > 1.0E-4 && pitchAcceleration > 2.0 && simple < 0.006 && simple > 0.0) {
            if (verbose++ > 3) {
                flag(playerData, new String[]{"GCD: " + simple, "PA: " + pitchAcceleration, "M: " + magic});
            }
        }
        else {
            verbose = 0;
        }
    }

    private long gcd(final long a, final long b) {
        if (b <= 16384L) {
            return a;
        }
        return this.gcd(b, a % b);
    }
}