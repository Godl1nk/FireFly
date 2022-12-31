package ac.firefly.check.impl.combat.aim;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import ac.firefly.data.PlayerData;
import ac.firefly.managers.PluginManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class AimA extends Check {

    public AimA() {
        super("Aim (A)", CheckType.COMBAT, true);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        PlayerData playerData = PluginManager.instance.getDataManager().getData(p);
        if (playerData.getLastAttack() > 100) {
            return;
        }
        float f = Math.abs(e.getTo().getYaw() - e.getFrom().getYaw());
        float f2 = Math.abs(e.getTo().getPitch() - e.getFrom().getPitch());
        if (f > 0.0f && (double) f < 0.01 && (double) f2 > 0.2) {
            flag(e.getPlayer(), "Y: " + f + " P: " + f2, "Y: " + f + " P: " + f2);
        }
    }
}
