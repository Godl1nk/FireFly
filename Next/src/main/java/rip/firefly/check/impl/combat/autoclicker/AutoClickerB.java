package rip.firefly.check.impl.combat.autoclicker;

import org.apache.commons.math3.stat.descriptive.moment.Kurtosis;
import org.apache.commons.math3.stat.descriptive.moment.Skewness;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.PacketCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.packet.tinyprotocol.api.NMSObject;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInArmAnimationPacket;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInFlyingPacket;
import rip.firefly.util.math.EvictingLinkedList;

@CheckData(name = "AutoClicker", subType = "B", type = CheckType.COMBAT, threshold = 10, description = "Checks For Invalid CPS Using Multiple Average Algorithms")
public class AutoClickerB extends PacketCheck {
    public AutoClickerB(PlayerData data) {
        super(data);
    }

    private double ticksArm;
    private double ticksFly;
    private EvictingLinkedList<Double> ticks = new EvictingLinkedList<>(100);

    @Override
    public void handle(PlayerData playerData, NMSObject packet) {
        if (packet instanceof WrappedInArmAnimationPacket) {
            this.ticksArm += 1.0;
        } else if (packet instanceof WrappedInFlyingPacket) {
            double d = this.ticksFly;
            this.ticksFly = d + 1.0;
            if (d >= 20.0) {
                double tpt = this.ticksArm / this.ticksFly;
                if (tpt <= 0.215) {
                    return;
                }
                this.ticks.add(tpt);
                double kurtosis = new Kurtosis().evaluate(this.ticks.stream().mapToDouble(Number::doubleValue).toArray());
                double skew = new Skewness().evaluate(this.ticks.stream().mapToDouble(Number::doubleValue).toArray());
                double variance = new Variance().evaluate(this.ticks.stream().mapToDouble(Number::doubleValue).toArray());
                if (this.ticks.size() >= 50 && (kurtosis < 0.0 || kurtosis > 1.798) && tpt > 0.0 && variance < 0.01 && skew > -0.1 && skew < 0.1) {
                    flag(playerData, String.format("K: %s", kurtosis), String.format("S: %s", skew), String.format("V: %s", variance));
                }
                this.ticksArm = 0.0;
                this.ticksFly = 0.0;
            }
        }
    }
}
