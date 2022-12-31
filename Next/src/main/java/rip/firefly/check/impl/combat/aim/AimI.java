package rip.firefly.check.impl.combat.aim;

import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.PacketCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.packet.tinyprotocol.api.NMSObject;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInFlyingPacket;
import rip.firefly.util.location.PlayerLocation;
import rip.firefly.util.math.MathUtil;

import java.util.ArrayList;
import java.util.List;

@CheckData(name = "Aim", subType = "I", description = "Checks For Rotation GCD Lower Than Possible", type = CheckType.COMBAT, threshold = 6, experimental = true)
public class AimI extends PacketCheck {
    public AimI(PlayerData data) {
        super(data);
    }

    private float lastPitchDiff;
    private List<Float> samples = new ArrayList();

    @Override
    public void handle(PlayerData playerData, NMSObject packet) {
        if(packet instanceof WrappedInFlyingPacket && ((WrappedInFlyingPacket)packet).isLook()) {
            boolean cinematic = playerData.isCinematic();

            if(cinematic) {
                return;
            }


            PlayerLocation to = playerData.getTo();
            PlayerLocation from = playerData.getFrom();
            float pitchDiff = Math.abs(to.getPitch() - from.getPitch());
            float yawDiff = Math.abs(to.getYaw() - from.getYaw());
            long expandedPitchDiff = (long)((double)pitchDiff * MathUtil.EXPANDER);
            long expandedLastPitchDiff = (long)((double)this.lastPitchDiff * MathUtil.EXPANDER);
            long gcd = MathUtil.gcd(16384L, expandedPitchDiff, expandedLastPitchDiff);
            if ((double)yawDiff > 0.9D && (double)pitchDiff < 15.0D && gcd < 131072L) {
                this.samples.add(yawDiff);
                if (incrementBuffer() > 20.0D) {
                    if (MathUtil.averageFloat(this.samples) > 11.0F) {
                        flag(playerData, String.format("PD: %s", pitchDiff), String.format("YD: %s", yawDiff), String.format("GCD: %s", gcd), String.format("B: %s", getBuffer()));
                    } else {
                        this.samples.clear();
                        resetBuffer();
                    }
                }
            } else if (getBuffer() > 0.0D) {
                decrementBuffer();
            }

            this.lastPitchDiff = pitchDiff;
        }
    }
}
