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

@CheckData(name = "Aim", subType = "X", description = "Checks If The Player's Rotation GCD Or Rotation Change Is Invalid", type = CheckType.COMBAT, threshold = 5)
public class AimX extends PacketCheck {

    private double multiplier = Math.pow(2.0, 24.0);
    private float previous;
    private List<Float> previousYaws = new ArrayList<Float>();

    public AimX(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(PlayerData playerData, NMSObject packet) {
        if (packet instanceof WrappedInFlyingPacket && ((WrappedInFlyingPacket) packet).isLook()) {

            if(playerData.isCinematic()) {
                return;
            }

            PlayerLocation to = playerData.getTo();
            PlayerLocation from = playerData.getFrom();
            float pitchChange = Math.abs(to.getPitch() - from.getPitch());
            float yawChange = Math.abs(to.getYaw() - from.getYaw());
            long a = (long)((double)pitchChange * this.multiplier);
            long b = (long)((double)this.previous * this.multiplier);
            long gcd = MathUtil.gcd(16384L, a, b);
            if ((double)yawChange > 0.9 && (double)pitchChange < 15.0 && gcd < 131072L) {
                if ((double)yawChange < 9.7 && (double)pitchChange > 0.05) {
                    this.previousYaws.add(yawChange);
                    if (incrementBuffer() > 17.0) {
                        if (averageFloat(this.previousYaws) > 0.0f) {
                            flag(playerData, String.format("YC: %s", yawChange), String.format("PC: %s", pitchChange), String.format("GCD: %s", gcd), String.format("YA: %s", averageFloat(this.previousYaws)));
                        } else {
                            resetBuffer();
                            this.previousYaws.clear();
                        }
                    }
                }
            } else if (getBuffer() > 0.0) {
                decrementBuffer();
            }
            this.previous = pitchChange;
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

    public static float averageFloat(List<Float> list) {
        float avg = 0.0f;
        for (float value : list) {
            avg += value;
        }
        if (list.size() > 0) {
            return avg / (float)list.size();
        }
        return 0.0f;
    }



}
