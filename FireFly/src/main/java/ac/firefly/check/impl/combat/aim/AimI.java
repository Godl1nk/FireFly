package ac.firefly.check.impl.combat.aim;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import ac.firefly.data.PlayerData;
import ac.firefly.managers.PluginManager;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class AimI extends Check {

    private float suspiciousYaw;

    public AimI() {
        super("Aim (I)", CheckType.COMBAT, true);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Location to = e.getTo();
        Location from = e.getFrom();
        PlayerData playerData = PluginManager.instance.getDataManager().getData(e.getPlayer());
        if (playerData.getLastAttack() > 20 * 60)
            return;

        float yawChange = Math.abs(to.getYaw() - from.getYaw());

        if (yawChange > 1F && Math.round(yawChange) == yawChange && yawChange % 1.5F != 0F) {
            if (yawChange == suspiciousYaw) {
                flag(e.getPlayer(), "Y: " + yawChange, "Y: " + yawChange);
            }

            suspiciousYaw = Math.round(yawChange);
        } else {
            suspiciousYaw = 0F;
        }
    }

}
