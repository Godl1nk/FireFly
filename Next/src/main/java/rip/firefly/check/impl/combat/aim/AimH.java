package rip.firefly.check.impl.combat.aim;

import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.PacketCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.packet.tinyprotocol.api.NMSObject;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInUseEntityPacket;

@CheckData(name = "Aim", subType = "H", description = "Checks For Badly Rounded Pitches", type = CheckType.COMBAT, threshold = 6)
public class AimH extends PacketCheck {
    private double threshold;

    public AimH(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(PlayerData playerData, NMSObject packet) {
        if(packet instanceof WrappedInUseEntityPacket && ((WrappedInUseEntityPacket)packet).getAction() == WrappedInUseEntityPacket.EnumEntityUseAction.ATTACK) {
            double pitch = Math.abs(playerData.getTo().getPitch() - playerData.getFrom().getPitch());

            if (pitch % 0.5 == 0.0 && pitch % 1.5f != 0.0) {
                if (threshold++ > 7) {
                    flag(playerData, String.format("T: %s", pitch), String.format("P: %s", pitch));
                }
            } else {
                threshold -= Math.min(threshold, 0.125);
            }
        }
    }
}
