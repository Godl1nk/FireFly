package rip.firefly.check.impl.misc.timer;

import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.PacketCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.packet.tinyprotocol.api.NMSObject;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInFlyingPacket;

@CheckData(name = "Timer", subType = "A", description = "Checks for game speed which is too fast", type = CheckType.MISC, threshold = 25)
public class TimerA extends PacketCheck {


    private long balance = 0L;
    private long lastFlying = 0L;

    public TimerA(PlayerData data) {
        super(data);
    }

    // TODO: HANDLE TELEPORTS
    @Override
    public void handle(PlayerData playerData, NMSObject fPacket) {
        if(fPacket instanceof WrappedInFlyingPacket) {
            final long now = System.currentTimeMillis();
            handle: {
                if((System.currentTimeMillis() - playerData.getLastJoin()) < 5000) break handle;
                if(lastFlying == 0) break handle;

                final long delay = now - lastFlying;
                balance += 50 - delay;

                if(balance > 5) {
                    if(incrementBuffer() > 5) {
                        flag(playerData, String.format("D: %s", delay), String.format("B: %s", balance));
                    }
                } else {
                    decrementBuffer();
                }
            }

            lastFlying = now;
        }
    }
}
