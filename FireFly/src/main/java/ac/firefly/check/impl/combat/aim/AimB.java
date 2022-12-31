package ac.firefly.check.impl.combat.aim;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import ac.firefly.data.PlayerData;
import ac.firefly.managers.PluginManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class AimB extends Check {

    public AimB() {
        super("Aim (B)", CheckType.COMBAT, true);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Location to = e.getTo();
        Location from = e.getFrom();
        Player p = e.getPlayer();
        PlayerData playerData = PluginManager.instance.getDataManager().getData(p);
        float f = Math.abs(to.getYaw() - from.getYaw());
        float f2 = Math.abs(to.getPitch() - from.getPitch());
        if (playerData.getLastAttackPacket() < 200) {
            if (f2 > 0.0f && f > 2.0f && (double) f2 < 0.0119) {
                flag(p, String.format("Y: %s P: %s", f, f2), String.format("Y: %s P: %s", f, f2));
            }
        }
    }
}
