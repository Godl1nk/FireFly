package rip.firefly.check.impl.combat.aura;

import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.PacketCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.packet.tinyprotocol.api.NMSObject;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInArmAnimationPacket;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInFlyingPacket;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInUseEntityPacket;

@CheckData(name = "Aura", subType = "A", description = "Checks If The Player's Rotation Is Invalid", type = CheckType.COMBAT, threshold = 10)
public class AuraA extends PacketCheck {

    public AuraA(PlayerData data) {
         super(data);
    }

    int swings;
    int attacks;
    int lastDiff;

    @Override
    public void handle(PlayerData data, NMSObject packet) {
        if(packet instanceof WrappedInFlyingPacket) {
            if (data.isLagging() || playerData.isDigging() || playerData.inLava || playerData.inWater || playerData.inWeb) {
                swings = 2;
                attacks = 2;
                return;
            }

            double pitch = Math.abs(data.getTo().getPitch() - data.getFrom().getPitch());
            double yaw = Math.abs(data.getTo().getYaw() - data.getFrom().getYaw());

            if ((System.currentTimeMillis() - data.getLastAttack() < 1000L)) {
                if (swings > 0 && attacks > 0) {
                    int diff = Math.abs(attacks - swings);

                    if (diff <= 0 && diff == lastDiff && pitch > 1 && yaw > 1.5) {
                        if (incrementBuffer() > 20) {
                            flag(playerData, new String[]{"D: " + diff, "S: " + swings, "A: " + attacks});
                        }
                    } else {
                        decreaseBuffer(0.5);
                    }

                    if (attacks > 50) {
                        swings = 0;
                        attacks = 0;
                    }
                    lastDiff = diff;
                }
            }
        } else if (packet instanceof WrappedInUseEntityPacket) {
            WrappedInUseEntityPacket useEntityPacket = (WrappedInUseEntityPacket) packet;
            if (useEntityPacket.getAction() == WrappedInUseEntityPacket.EnumEntityUseAction.ATTACK) {
                attacks++;
            }
        } else if (packet instanceof WrappedInArmAnimationPacket) {
            swings++;
        }
    }
}
