package rip.firefly.check.impl.combat.autoclicker;

import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.PacketCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.packet.tinyprotocol.api.NMSObject;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInArmAnimationPacket;

import java.util.Deque;
import java.util.LinkedList;

@CheckData(name = "AutoClicker", subType = "A", type = CheckType.COMBAT, threshold = 10, description = "Checks For Low Outliers In CPS")
public class AutoClickerA extends PacketCheck {
    public AutoClickerA(PlayerData data) {
        super(data);
        this.ticks = new LinkedList<>();
        this.buffer = 0.0;
    }

    private final Deque<Double> ticks;
    private double lastDeviation;
    private double buffer;

    @Override
    public void handle(PlayerData playerData, NMSObject packet) {
        if(packet instanceof WrappedInArmAnimationPacket) {
            if(!playerData.isDigging()) {
                this.ticks.add((this.playerData.getCurrentTick() * 50.0));
            }
            if (this.ticks.size() >= 10) {
                final double deviation = getStandardDeviation(this.ticks.stream().mapToDouble(d -> d).toArray());
                final double diff = Math.abs(deviation - this.lastDeviation);
                if (diff < 10.0) {
                    final double buffer = this.buffer + 1.0;
                    this.buffer = buffer;
                    if (buffer > 5.0) {
                        flag(playerData, String.format("D: %s", diff));
                    }
                }
                else {
                    this.buffer *= 0.75;
                }
                this.lastDeviation = deviation;
                this.ticks.clear();
            }
        }
    }

    public static double getStandardDeviation(final double[] numberArray) {
        double sum = 0.0;
        double deviation = 0.0;
        final int length = numberArray.length;
        for (final double num : numberArray) {
            sum += num;
        }
        final double mean = sum / length;
        for (final double num2 : numberArray) {
            deviation += Math.pow(num2 - mean, 2.0);
        }
        return Math.sqrt(deviation / length);
    }
}
