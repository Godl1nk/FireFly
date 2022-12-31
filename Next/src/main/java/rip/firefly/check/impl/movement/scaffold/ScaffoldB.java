package rip.firefly.check.impl.movement.scaffold;

import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.PacketCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.packet.tinyprotocol.api.NMSObject;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInBlockPlacePacket;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInEntityActionPacket;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInFlyingPacket;

@CheckData(name = "Scaffold", subType = "B", description = "Checks If the player doesn't sneak neither jump after specified block places", threshold = 10, type = CheckType.MOVEMENT)
public class ScaffoldB extends PacketCheck {

    private int blockPlaces = 0;
    private double lastY = -1;

    public ScaffoldB(PlayerData data) {
        super(data);
    }

    // TODO: Rewrite this check and make it more efficient
    @Override
    public void handle(PlayerData playerData, NMSObject packet) {
        if (packet.getObject() instanceof WrappedInFlyingPacket) {
            WrappedInFlyingPacket wrapped = (WrappedInFlyingPacket) packet.getObject();
            if (lastY == -1) {
                lastY = wrapped.getY();
            }
            if (lastY - wrapped.getY() != 0) {
                blockPlaces = 0;
            }
        }
        if (packet.getObject() instanceof WrappedInEntityActionPacket) {
            WrappedInEntityActionPacket wrapped = (WrappedInEntityActionPacket) packet.getObject();
            if (wrapped.getAction() == WrappedInEntityActionPacket.EnumPlayerAction.START_SNEAKING) {
                blockPlaces = 0;
            }
        }
        if (packet.getObject() instanceof WrappedInBlockPlacePacket) {
            blockPlaces++;
        }
        if (blockPlaces >= 30) {
            flag(playerData, String.format("BP: %s", blockPlaces));
        }
    }

}
