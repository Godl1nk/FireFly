package rip.firefly.check.impl.combat.aim;

import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.PacketCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.packet.tinyprotocol.api.NMSObject;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInFlyingPacket;

@CheckData(name = "Aim", subType = "Y", description = "Checks If A Player's Yaw And Pitch Are Too Consistent", type = CheckType.COMBAT, threshold = 10 )
public class AimY extends PacketCheck {

    float lastPitch, lastYaw, a, lastDeltaYaw, lastDeltaPitch;
   
    public AimY(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(PlayerData playerData, NMSObject fpacket) {
        if (fpacket instanceof WrappedInFlyingPacket && ((WrappedInFlyingPacket) fpacket).isLook()) {
            WrappedInFlyingPacket packet =  (WrappedInFlyingPacket) fpacket;
            float yaw = packet.getYaw();
            float pitch = packet.getPitch();
            if (yaw == 0 || pitch == 0) return;
            float deltaPitch = Math.abs(pitch - lastPitch);
            float deltaYaw = Math.abs(yaw - lastYaw);
            if (deltaYaw == lastDeltaYaw && pitch < 90 && deltaYaw > .03 && deltaYaw != 0 && lastDeltaYaw != 0) incrementBuffer();
            else {
                if (getBuffer() > 0) decrementBuffer();
            }
            if (getBuffer() > 5) {
                flag(playerData, new String[]{"DY: " + deltaYaw, "DP: " + deltaPitch});
                resetBuffer();
            }
            lastDeltaYaw = deltaYaw;
            lastDeltaPitch = deltaPitch;
            lastYaw = yaw;
            lastPitch = pitch;
        }
    }
}