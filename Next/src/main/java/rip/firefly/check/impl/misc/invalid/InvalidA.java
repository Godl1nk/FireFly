package rip.firefly.check.impl.misc.invalid;

import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.PacketCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.packet.tinyprotocol.api.NMSObject;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInFlyingPacket;

@CheckData(name = "Invalid", subType = "A", description = "Checks For Invalid Pitch Packets", type = CheckType.MISC, threshold = 1)
public class InvalidA extends PacketCheck {

    public InvalidA(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(PlayerData playerData, NMSObject fPacket) {
        if(fPacket instanceof WrappedInFlyingPacket) {
            WrappedInFlyingPacket packet = (WrappedInFlyingPacket)fPacket;
            if (Math.abs(packet.getPitch()) > 90.0f) {
                flag(playerData, String.format("P: %s", packet.getPitch()));
            }
        }
    }
}
