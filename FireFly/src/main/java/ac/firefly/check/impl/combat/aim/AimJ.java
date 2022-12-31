package ac.firefly.check.impl.combat.aim;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import ac.firefly.events.packet.PacketFlyingEvent;
import org.bukkit.event.EventHandler;

public class AimJ extends Check {

    private float previousPitch, previousYaw, pitch, yaw;

    public AimJ() {
        super("Aim (J)", CheckType.COMBAT, true);
    }

    @EventHandler
    public void onMove(PacketFlyingEvent e) {
        if (!e.isLook()) {
            return;
        }

        float pitch = e.getPitch();
        float yaw = e.getYaw();
        float pitchChange = Math.abs(pitch - this.pitch);
        float yawChange = Math.abs(yaw - this.yaw);
        float pitchDifference = Math.abs(previousPitch - pitchChange);

        float yawDifference = Math.abs(previousYaw - yawChange);

        //Emulating mouse moves
        if (yawChange > yawDifference && yawDifference > 0.3 && pitchChange > 0 && pitchChange <= pitchDifference && pitchDifference < 0.1) {
            flag(e.getPlayer(), "Fabricated", "");
        }

        //Flaws in randomization
        if (yawChange > yawDifference && yawDifference > 0.0 && yawDifference < 0.1 && pitchChange > 0.08) {
            flag(e.getPlayer(), "Poor Randomization", "");
        }

        //Extremely randomized
        if (yawChange > yawDifference && yawDifference > 0.0 && pitchChange > 0 && pitchChange < 0.02 && pitchDifference > pitchChange * 2) {
            flag(e.getPlayer(), "Randomized", "");
        }

        //Turning aim assist on and off
        if (yawDifference > 900 && pitchChange > 0 && pitchDifference < 10) {
            flag(e.getPlayer(), "Toggle", "");
        }

        //Rounded yaw
        if (yawDifference > 0 && Math.abs(Math.floor(yawDifference) - yawDifference) < 0.0000000001) {
            flag(e.getPlayer(), "Rounded", "");
        }

        this.pitch = pitch;
        this.yaw = yaw;
        previousPitch = pitchChange;
        previousYaw = yawChange;
    }

}
