package ac.firefly.check.impl.combat.aim;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import ac.firefly.data.PlayerData;
import ac.firefly.managers.PluginManager;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Deque;
import java.util.LinkedList;

public class AimE extends Check {

    private int buffer;
    private final Deque<Float> pitchSamples = new LinkedList<>();

    public AimE() {
        super("Aim (E)", CheckType.COMBAT, true);
    }


    public void onMove(PlayerMoveEvent e) {
        Location to = e.getTo();
        Location from = e.getFrom();
        PlayerData playerData = PluginManager.instance.getDataManager().getData(e.getPlayer());
        final float deltaYaw = Math.abs(to.getYaw() - from.getYaw());
        final float deltaPitch = Math.abs(to.getPitch() - from.getPitch());

        if (deltaYaw > 1.0 && deltaPitch > 0.0 && deltaPitch % 1.0 == 0.0) {
            buffer += 5;

            if (buffer > 15) {
                flag(e.getPlayer(), String.format("Y: %s P: %s", deltaYaw, deltaPitch), null);
            }
        } else {
            buffer = 0;
        }

        if (deltaYaw > 1.0 && deltaPitch > 0.0 && deltaYaw % 1.0 == 0.0) {
            buffer += 5;

            if (buffer > 15) {
                flag(e.getPlayer(), String.format("Y: %s P: %s", deltaYaw, deltaPitch), null);
            }
        } else {
            buffer = 0;
        }

        if (deltaYaw > 1.0 && deltaPitch > 0.0 && deltaPitch % 10.0 == 0.0) {
            buffer += 5;

            if (buffer > 15) {
                flag(e.getPlayer(), String.format("Y: %s P: %s", deltaYaw, deltaPitch), null);
            }
        }

        if (deltaYaw > 1.0 && deltaPitch > 0.0 && deltaYaw % 10.0 == 0.0) {
            buffer += 5;

            if (buffer > 15) {
                flag(e.getPlayer(), String.format("Y: %s P: %s", deltaYaw, deltaPitch), null);
            }
        }
    }
}
