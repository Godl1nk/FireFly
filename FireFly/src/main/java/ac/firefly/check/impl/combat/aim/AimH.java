package ac.firefly.check.impl.combat.aim;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class AimH extends Check {

    private float lastYawChange;

    public AimH() {
        super("Aim (H)", CheckType.COMBAT, false);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {

    }
}
