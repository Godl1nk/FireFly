package rip.firefly.check.impl.combat.aim;

import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.PacketCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.packet.tinyprotocol.api.NMSObject;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInFlyingPacket;
import rip.firefly.util.math.TimerUtil;

@CheckData(name = "Aim", subType = "E", description = "Checks For Pitch Snapping", type = CheckType.COMBAT, threshold = 8)
public class AimE extends PacketCheck {


    public AimE(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(PlayerData playerData, NMSObject packet) {
        if (packet instanceof WrappedInFlyingPacket && ((WrappedInFlyingPacket) packet).isLook()) {
            if (!TimerUtil.elapsed(playerData.getLastAttack(), 200)) {

                double deltaYaw = playerData.movement.deltaYaw;
                double deltaPitch = playerData.movement.deltaPitch;

                if (deltaPitch < 0.05f && deltaPitch > 0.009f && playerData.movement.lastDeltaPitch < 0.05f
                        && playerData.movement.lastDeltaPitch > 0.009f && deltaYaw > 6.2f && playerData.movement.lastDeltaYaw > 0.4f) {
                    flag(playerData, String.format("DY: %s", deltaYaw), String.format("DP: %s", deltaPitch));
                }

           }

        }
    }

}