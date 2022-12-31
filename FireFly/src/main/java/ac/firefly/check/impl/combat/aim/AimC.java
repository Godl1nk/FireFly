package ac.firefly.check.impl.combat.aim;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import ac.firefly.data.PlayerData;
import ac.firefly.managers.PluginManager;
import ac.firefly.util.interaction.ServerUtils;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Deque;
import java.util.LinkedList;

public class AimC extends Check {

    private int buffer;
    private final Deque<Float> pitchSamples = new LinkedList<>();

    public AimC() {
        super("Aim (C)", CheckType.COMBAT, true);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Location to = e.getTo();
        Location from = e.getFrom();
        PlayerData playerData = PluginManager.instance.getDataManager().getData(e.getPlayer());
        final float deltaYaw = Math.abs(to.getYaw() - from.getYaw());
        final float deltaPitch = Math.abs(to.getPitch() - from.getPitch());

        if (deltaPitch > 0.0 && deltaYaw > 0.0 && deltaYaw < 25.f && deltaPitch < 20.f) {
            final boolean cinematic = playerData.isCinematic();

            if (!cinematic && pitchSamples.add(deltaPitch) && pitchSamples.size() == 120) {
                final long distinct = pitchSamples.stream().distinct().count();
                final long duplicates = pitchSamples.size() - distinct;

                if (duplicates <= 9L) {

                    if (++buffer > 2) {
                        flag(e.getPlayer(), "", null);
                    }

                    if (duplicates <= 3) {
                        buffer += 2;
                    }
                } else {
                    buffer = 0;
                }

                pitchSamples.clear();
            }
        }
    }
}
