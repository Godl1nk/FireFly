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

public class AimG extends Check {

    private final Deque<Double> deltaXSamples = new LinkedList<>();
    private final Deque<Double> deltaYSamples = new LinkedList<>();

    private int buffer;

    public AimG() {
        super("Aim (G)", CheckType.COMBAT, true);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Location to = e.getTo();
        Location from = e.getFrom();
        PlayerData playerData = PluginManager.instance.getDataManager().getData(e.getPlayer());
        final long now = System.currentTimeMillis();

        final float deltaYaw = Math.abs(to.getYaw() - from.getYaw());
        final float deltaPitch = Math.abs(to.getPitch() - from.getPitch());

        final boolean attacking = now - playerData.getLastAttackPacket() < 1000L;

        if (deltaYaw == 0.0 || !attacking) {
            buffer = 0;
        }

        if (deltaPitch == 0.0 && deltaYaw > 0.1) {
            if (++buffer > 49) {
                flag(e.getPlayer(), String.format("Y: %s P: %s", deltaYaw, deltaPitch), null);
            }
        } else {
            buffer = Math.max(buffer - 1, 0);
        }
    }
}
