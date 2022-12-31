package rip.firefly.check.impl.combat.aim;

import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.PacketCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.packet.tinyprotocol.api.NMSObject;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInUseEntityPacket;
import rip.firefly.util.location.PlayerLocation;
import rip.firefly.util.math.IntUtil;

import java.util.ArrayList;
import java.util.List;

@CheckData(name = "Aim", subType = "G", description = "Checks For Cinematic Camera", type = CheckType.COMBAT, threshold = 6)
public class AimG extends PacketCheck {
    private long lastSmooth, lastHighRate;
    private double lastDeltaYaw, lastDeltaPitch;

    private final List<Double> yawSamples = new ArrayList<>();
    private final List<Double> pitchSamples = new ArrayList<>();

    public AimG(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(PlayerData playerData, NMSObject packet) {
        if(packet instanceof WrappedInUseEntityPacket && ((WrappedInUseEntityPacket)packet).getAction() == WrappedInUseEntityPacket.EnumEntityUseAction.ATTACK) {
            final long now = System.currentTimeMillis();

            final PlayerLocation from = playerData.getFrom();
            final PlayerLocation to = playerData.getTo();

            final double deltaYaw = Math.abs(to.getYaw() - from.getYaw());
            final double deltaPitch = Math.abs(to.getPitch() - from.getPitch());

            final double differenceYaw = Math.abs(deltaYaw - lastDeltaYaw);
            final double differencePitch = Math.abs(deltaPitch - lastDeltaPitch);

            final double joltYaw = Math.abs(differenceYaw - deltaYaw);
            final double joltPitch = Math.abs(differencePitch - deltaPitch);

            final boolean cinematic = (now - lastHighRate > 250L) || now - lastSmooth < 9000L;

            if (joltYaw > 1.0 && joltPitch > 1.0) {
                this.lastHighRate = now;
            }

            if (deltaPitch > 0.0 && deltaPitch > 0.0) {
                yawSamples.add(deltaYaw);
                pitchSamples.add(deltaPitch);
            }

            if (yawSamples.size() == 40 && pitchSamples.size() == 40) {
                // Get the cerberus/positive graph of the sample-lists
                final IntUtil.GraphResult resultsYaw = IntUtil.getGraph(yawSamples);
                final IntUtil.GraphResult resultsPitch = IntUtil.getGraph(pitchSamples);

                // Negative values
                final int negativesYaw = resultsYaw.getNegatives();
                final int negativesPitch = resultsPitch.getNegatives();

                // Positive values
                final int positivesYaw = resultsYaw.getPositives();
                final int positivesPitch = resultsPitch.getPositives();

                // Cinematic camera usually does this on *most* speeds and is accurate for the most part.
                if (positivesYaw > negativesYaw || positivesPitch > negativesPitch) {
                    this.lastSmooth = now;
                }

                yawSamples.clear();
                pitchSamples.clear();
            }

            if(cinematic) {
                incrementBuffer();
            } else {
                decrementBuffer();
            }

            if(getBuffer() > 20) {
            //    flag(playerData, String.format("B: %s", buffer));
                playerData.setCinematic(cinematic);
            }
            //TODO: MAKE MORE ACCURATE!!!
            //playerData.setCinematic(cinematic);
            playerData.setCinematic(false);
            this.lastDeltaYaw = deltaYaw;
            this.lastDeltaPitch = deltaPitch;
        }
    }
}
