package rip.firefly.check.impl.misc.invalid;

import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.PacketCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.packet.tinyprotocol.api.NMSObject;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInUseEntityPacket;

@CheckData(name = "Invalid", subType = "B", description = "Checks For Invalid Hit Packets", type = CheckType.MISC, threshold = 1)
public class InvalidB extends PacketCheck {

    public InvalidB(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(PlayerData playerData, NMSObject fPacket) {
        if(fPacket instanceof WrappedInUseEntityPacket) {
            WrappedInUseEntityPacket packet = (WrappedInUseEntityPacket)fPacket;
            if(packet.getAction() == WrappedInUseEntityPacket.EnumEntityUseAction.ATTACK) {
                if(packet.getEntity().getEntityId() == fPacket.getPlayer().getEntityId()) {
                    flag(playerData, String.format("EI: %s", packet.getEntity().getEntityId()));
                }
            }
        }
    }
}
