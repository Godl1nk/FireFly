package rip.firefly.check.impl.misc.payload;

import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.PacketCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.packet.tinyprotocol.api.NMSObject;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInCustomPayload;

import java.util.Objects;

@CheckData(name = "Payload", subType = "C", description = "Checks for Lite Client", type = CheckType.MISC, threshold = 1)
public class PayloadC extends PacketCheck {

    public PayloadC(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(PlayerData playerData, NMSObject packet) {
        if(packet instanceof WrappedInCustomPayload) {
            WrappedInCustomPayload wrapper = (WrappedInCustomPayload) packet;

            if(Objects.equals(wrapper.getChannel(), "218c69d8875f")) {
                flag(playerData);
            }
        }
    }

}