package rip.firefly.check.impl.misc.ping;

import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.PacketCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.packet.tinyprotocol.api.NMSObject;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInKeepAlivePacket;

@CheckData(name = "Ping", subType = "C", description = "Checks If A Player Sends 2 Keep Alive Packets With The Same ID", type = CheckType.MISC, threshold = 2)
public class PingC extends PacketCheck {


    public PingC(PlayerData data) {
        super(data);
    }

    private int lastId;

    @Override
    public void handle(PlayerData data, NMSObject packet) {
        if (packet instanceof WrappedInKeepAlivePacket) {
            try {
                int id = (int)((WrappedInKeepAlivePacket)packet).getTime();
                if (id == this.lastId) {
                    flag(playerData, String.format("ID: %s", id), String.format("LID: %s", lastId));
                }
                this.lastId = id;
            }
            catch (Exception ignored) {}
        }
    }
}
