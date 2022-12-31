package rip.firefly.check.impl.misc.payload;

import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.PacketCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.packet.tinyprotocol.api.NMSObject;

@CheckData(name = "Payload", subType = "B", description = "Checks for invalid client brands", type = CheckType.MISC, threshold = 1)
public class PayloadB extends PacketCheck {

    public PayloadB(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(PlayerData playerData, NMSObject packet) {

    }

    public void flag(PlayerData data, String checkData) {
        super.flag(data, new String[]{checkData});
    }
}