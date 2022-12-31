package rip.firefly.check.impl.combat.aim;

import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.PacketCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.packet.tinyprotocol.api.NMSObject;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInFlyingPacket;
import rip.firefly.util.math.MathUtil;
import rip.firefly.util.misc.EvictingList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@CheckData(name = "Aim", subType = "L", description = "Checks Player's Mouse Sensitivity", type = CheckType.COMBAT, threshold = 10 )
public class AimL extends PacketCheck {

    private static Map<Double, Double> sensitivityMap = new HashMap();
    private final EvictingList<Double> samples = new EvictingList(50);
    private double lastDeltaPitch;
    private double lastMode;
    private double lastConstant;
    private double recordedConstant;

    static {
        for(double d = 50.9D; d < 204.8D; d += 0.7725D) {
            sensitivityMap.put(d, sensitivityMap.size() * 0.005D);
        }

    }

    public AimL(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(PlayerData playerData, NMSObject packet) {
        if (packet instanceof WrappedInFlyingPacket && ((WrappedInFlyingPacket) packet).isLook()) {
            double deltaPitch = Math.abs(playerData.getTo().getPitch() - playerData.getFrom().getPitch());
            long expandedPitch = (long)Math.abs(deltaPitch * MathUtil.EXPANDER);
            long lastExpandedPitch = (long)Math.abs(this.lastDeltaPitch * MathUtil.EXPANDER);
            double pitchGcd = (double)MathUtil.getGcd(expandedPitch, lastExpandedPitch) / MathUtil.EXPANDER;
            this.samples.add(pitchGcd);
            if (this.samples.size() == 50) {
                double mode = MathUtil.getModeD(this.samples);
                long expandedMode = (long)(mode * MathUtil.EXPANDER);
                long lastExpandedMode = (long)(this.lastMode * MathUtil.EXPANDER);
                double modeGcd = (double)MathUtil.getGcd(expandedMode, lastExpandedMode);
                double constant = (double)Math.round((Math.cbrt(modeGcd / 0.15D / 8.0D) - 0.33333333333333337D) * 1000.0D) / 1000.0D;
                if (Math.abs(constant - this.lastConstant) < 0.01D) {
                    this.playerData.setVerifyingSensitivity(false);
                    this.recordedConstant = constant;
                } else {
                    this.playerData.setVerifyingSensitivity(true);
                }

                double sensitivity = this.getSensitivity(this.recordedConstant);
                if (sensitivity > -1.0D) {
                    this.playerData.setSensitivity(sensitivity);
                }

                this.lastConstant = constant;
                this.lastMode = mode;
                this.samples.clear();
            }

            this.lastDeltaPitch = deltaPitch;
        }
    }

    public double getSensitivity(double constant) {
        Iterator<Double> var3 = sensitivityMap.keySet().iterator();

        double val;
        do {
            if (!var3.hasNext()) {
                return -1.0D;
            }

            val = var3.next();
        } while(Math.abs(val - constant) > 0.4D);

        return sensitivityMap.get(val);
    }
}