package ac.firefly.check.impl.movement.speed;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

public class SpeedH extends Check {
    private Double lastAngle = null;
    private int threshold = 0;


    public SpeedH() {
        super("Speed (H)", CheckType.MOVEMENT, true);
    }

    public void onMove(PlayerMoveEvent e) {
        Location from = e.getFrom();
        Location to = e.getTo();
        Player p = e.getPlayer();

        double deltaX = to.getX() - from.getX();
        double deltaZ = to.getZ() - from.getZ();

        float yaw = Math.abs(from.getYaw()) % 360;

        if (!p.isSprinting()) {
            return;
        }

        if (deltaX < 0.0 && deltaZ > 0.0 && yaw > 180.0f && yaw < 270.0f || deltaX < 0.0 && deltaZ < 0.0 && yaw > 270.0f && yaw < 360.0f || deltaX > 0.0 && deltaZ < 0.0 && yaw > 0.0f && yaw < 90.0f || deltaX > 0.0 && deltaZ > 0.0 && yaw > 90.0f && yaw < 180.0f) {
            flag(p, "OmniSprint", String.format("X: %s Z: %s", deltaX, deltaZ));
        }
    }

}
