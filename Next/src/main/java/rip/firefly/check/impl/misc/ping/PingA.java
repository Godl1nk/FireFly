package rip.firefly.check.impl.misc.ping;

import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.PacketCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.packet.tinyprotocol.api.NMSObject;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInKeepAlivePacket;

@CheckData(name = "Ping", subType = "A", description = "Checks If A Player Is Spoofing Their Ping (Sigma)", type = CheckType.MISC, threshold = 7)
public class PingA extends PacketCheck {


    public PingA(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(PlayerData playerData, NMSObject packet) {
        if (packet instanceof WrappedInKeepAlivePacket) {
            try {
                int id = (int)((WrappedInKeepAlivePacket)packet).getTime();
                if (id == 10000 && incrementBuffer() > 1) {
                    flag(playerData, String.format("I: %s", id), String.format("B: %s", getBuffer()));
                    resetBuffer();
                }
            }
            catch (Exception e) {
                return;
            }
        }
    }
}
