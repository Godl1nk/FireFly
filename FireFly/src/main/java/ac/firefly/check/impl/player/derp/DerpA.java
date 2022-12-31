package ac.firefly.check.impl.player.derp;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class DerpA extends Check {
    public DerpA() {
        super("Derp (A)", CheckType.MOVEMENT, true);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        double Pitch = e.getPlayer().getLocation().getPitch();
        if (Pitch > 90 || Pitch < -90) {
            flag(e.getPlayer(), "Derp/Head Roll", "Pitch: " + Pitch);
        }
    }
}
