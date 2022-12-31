package ac.firefly.check.impl.combat.aim;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import ac.firefly.data.PlayerData;
import ac.firefly.managers.PluginManager;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Deque;
import java.util.LinkedList;

public class AimF extends Check {

    public AimF() {
        super("Aim (F)", CheckType.COMBAT, true);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Location to = e.getTo();
        Location from = e.getFrom();
        PlayerData playerData = PluginManager.instance.getDataManager().getData(e.getPlayer());
        final float deltaYaw = Math.abs(to.getYaw() - from.getYaw());
        final float deltaPitch = Math.abs(to.getPitch() - from.getPitch());

        if (System.currentTimeMillis() - playerData.getLastAttackPacket() < 30 && deltaYaw > 0.0f && deltaYaw < 0.8 && deltaPitch > 0.279 && deltaPitch < 0.28090858) {
            flag(e.getPlayer(), String.format("Y: %s P: %s", deltaYaw, deltaPitch), String.format("Y: %s P: %s", deltaYaw, deltaPitch));
        }
    }
}
