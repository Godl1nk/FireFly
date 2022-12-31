package rip.firefly.check.impl.misc.invalid;

import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.PacketCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.packet.tinyprotocol.api.NMSObject;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInBlockDigPacket;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInFlyingPacket;

@CheckData(name = "Invalid", subType = "C", description = "Checks If A Player Is Digging On Post", type = CheckType.MISC, threshold = 1)
public class InvalidC extends PacketCheck {

    public InvalidC(PlayerData data) {
        super(data);
    }

    private boolean sent;
    public long lastFlying, lastPacket;

    @Override
    public void handle(PlayerData playerData, NMSObject fPacket) {
        if(fPacket instanceof WrappedInBlockDigPacket) {

            final long now = System.currentTimeMillis();
            final long delay = now - lastFlying;

            if (delay < 10L) {
                lastPacket = now;
                sent = true;
            } else {
                decreaseBuffer(0.0025);
            }
        } else if(fPacket instanceof WrappedInFlyingPacket) {
            final long now = System.currentTimeMillis();
            final long delay = now - lastPacket;

            if (sent) {
                if (delay > 40L && delay < 100L) {
                    increaseBuffer(0.25);

                    if (getBuffer() > 0.75) {
                        flag(playerData, String.format("B: %s", delay),  String.format("D: %s", delay));
                    }
                } else {
                    decreaseBuffer(0.025);
                }

                sent = false;
            }

            this.lastFlying = now;
        }
    }
}
