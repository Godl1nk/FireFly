package rip.firefly.check.impl.combat.aim;

import org.bukkit.util.Vector;
import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.PacketCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.packet.tinyprotocol.api.NMSObject;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInFlyingPacket;

@CheckData(name = "Aim", subType = "A", description = "Checks For Low Deviation In The Player's Rotation", type = CheckType.COMBAT, threshold = 7)
public class AimA extends PacketCheck {
    private final double multiplier = Math.pow(2.0, 24.0);
    private float lastPitch = -1;
    private final long[] gcdLog = new long[10];
    private int current = 0;

    public AimA(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(PlayerData playerData, NMSObject packet) {
        if(packet instanceof WrappedInFlyingPacket && ((WrappedInFlyingPacket)packet).isLook()) {
            Vector first = new Vector(playerData.movement.deltaYaw, 0, playerData.movement.deltaPitch);
            Vector second = new Vector(playerData.movement.lastDeltaYaw, 0, playerData.movement.lastDeltaPitch);

            double angle = Math.pow(first.angle(second) * 180, 2);

            if(playerData.isCinematic()) return;

            long deviation = getDeviation(playerData.movement.deltaPitch);

            gcdLog[current % gcdLog.length] = deviation;
            current++;

            if (playerData.movement.tpitch > -20 && playerData.movement.tpitch < 20
                    && playerData.movement.deltaPitch > 0
                    && playerData.movement.deltaYaw > 1
                    && playerData.movement.deltaYaw < 10
                    && playerData.movement.lastDeltaYaw <= playerData.movement.deltaYaw
                    && playerData.movement.yawDifference != 0
                    && playerData.movement.yawDifference < 1
                    && angle > 2500
            ) {

                long maxDeviation = 0;
                if (current > gcdLog.length) {
                    for (long l : gcdLog)
                        if (deviation != 0 && l != 0)
                            maxDeviation = Math.max(Math.max(l, deviation) % Math.min(l, deviation), maxDeviation);
                }
                if (deviation > 0) {
                    flag(playerData, String.format("MD: %s", maxDeviation), String.format("D: %s", deviation), String.format("DY: %s", playerData.movement.deltaYaw), String.format("DP: %s", playerData.movement.deltaPitch));
                    reset();
                }
            }
        }
    }

    public long getDeviation(float pitchChange) {
        if (lastPitch != -1) {
            try {
                long current = (long) (pitchChange * multiplier);
                long last = (long) (lastPitch * multiplier);
                long value = convert(current, last);

                if (value < 0x20000) {
                    return value;
                }
            } catch (Exception ignored) {
            }
        }

        lastPitch = pitchChange;
        return -1;
    }

    public void reset() {
        lastPitch = -1;
    }

    private long convert(long current, long last) {
        if (last <= 16384) return current;
        return convert(last, current % last);
    }
}
