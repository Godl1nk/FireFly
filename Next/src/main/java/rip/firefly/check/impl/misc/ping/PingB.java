package rip.firefly.check.impl.misc.ping;

import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.PacketCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.packet.tinyprotocol.api.NMSObject;

@CheckData(name = "Ping", subType = "B", description = "Checks If A Player Is Spoofing Their Ping", type = CheckType.MISC, threshold = 7)
public class PingB extends PacketCheck {


    public PingB(PlayerData data) {
        super(data);
    }

    private int vl;

    @Override
    public void handle(PlayerData data, NMSObject packet) {

        if (data.getLastClientKeepAlive() == 0L || data.getLastServerKeepAlive() < 500L) {
            return;
        }
        long one = System.currentTimeMillis() - data.getLastClientKeepAlive();
        long two = System.currentTimeMillis() - data.getLastServerKeepAlive();
        int n = this.vl = one > 7000L && two < 3000L ? this.vl + 3 : this.vl - 20;
        if (this.vl > 5) {
            flag(playerData, String.format("B: %s", buffer), String.format("CD: %s", one), String.format("SD: %s", two));
            this.vl = 0;
        }
    }
}
