package rip.firefly.check.impl.misc.baritone;

import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.PacketCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.packet.tinyprotocol.api.NMSObject;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInFlyingPacket;

@CheckData(name = "Baritone", subType = "A", description = "Checks Baritone-like Pitch Rounding", type = CheckType.MISC, threshold = 8, experimental = true)
public class BaritoneA extends PacketCheck {

    public BaritoneA(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(PlayerData playerData, NMSObject packet) {
        if (packet instanceof WrappedInFlyingPacket && ((WrappedInFlyingPacket) packet).isLook() && Math.abs(playerData.movement.deltaPitch) >= 0.5f) {

            float deltaPitchM1 = playerData.movement.deltaPitch % 0.1f, deltaPitchM5 = playerData.movement.deltaPitch % 0.05f;
            if(playerData.movement.deltaPitch > 0 && Math.abs(playerData.movement.deltaPitch) < 100
                    && (deltaPitchM1 == 0 || deltaPitchM5 == 0 || playerData.movement.deltaPitch % 1f == 0)) {
                if(increaseBuffer(1) > 10) {
                    flag(playerData, String.format("P: %s", playerData.movement.deltaPitch), String.format("B: %s", getBuffer()));
                    resetBuffer();
                }
            } else decreaseBuffer(0.25);
        }
    }



}
