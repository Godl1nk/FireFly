package rip.firefly.check.impl.combat.aim;

import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.PacketCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.packet.tinyprotocol.api.NMSObject;

@CheckData(name = "Aim", subType = "C", description = "Checks For Rounded Pitches", type = CheckType.COMBAT, threshold = 8)
public class AimC extends PacketCheck {

    public AimC(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(PlayerData playerData, NMSObject packet) {
        // TODO
    }

    public float getGcd(float current, float previous) {
        float temp;

        if (previous > current) {
            temp = current;
            current = previous;
            previous = temp;
        }

        while (previous > 16384L) {
            temp = current % previous;
            current = previous;
            previous = temp;
        }

        return current;
    }

}
