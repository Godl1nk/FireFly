package rip.firefly.check.impl.combat.aim;

import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.PacketCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.packet.tinyprotocol.api.NMSObject;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInFlyingPacket;
import rip.firefly.util.math.IntUtil;

import java.util.ArrayList;
import java.util.List;

@CheckData(name = "Aim", subType = "M", description = "Checks For Cinematic Camera", type = CheckType.COMBAT, threshold = 6)
public class AimM extends PacketCheck {
    private long lastSmooth, lastHighRate;
    private double lastDeltaYaw, lastDeltaPitch;

    private final List<Double> yawSamples = new ArrayList<>();
    private final List<Double> pitchSamples = new ArrayList<>();

    public AimM(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(PlayerData playerData, NMSObject packet) {
        if(packet instanceof WrappedInFlyingPacket && ((WrappedInFlyingPacket)packet).isLook()) {
            // TODO
        }
    }
}
