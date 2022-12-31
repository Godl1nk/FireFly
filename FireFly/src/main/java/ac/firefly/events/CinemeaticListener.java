package ac.firefly.events;

import ac.firefly.data.PlayerData;
import ac.firefly.managers.PluginManager;
import ac.firefly.util.math.GraphUtil;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.List;

public class CinemeaticListener implements Listener {
    private long lastSmooth, lastHighRate;
    private double lastDeltaYaw, lastDeltaPitch;

    private final List<Double> yawSamples = new ArrayList<>();
    private final List<Double> pitchSamples = new ArrayList<>();

    @EventHandler
    public void onMove(PlayerMoveEvent e) {

        PlayerData playerData = PluginManager.instance.getDataManager().getData(e.getPlayer());

        final long now = System.currentTimeMillis();

        final Location from = e.getFrom();
        final Location to = e.getTo();

        final double deltaYaw = Math.abs(to.getYaw() - from.getYaw());
        final double deltaPitch = Math.abs(to.getPitch() - from.getPitch());

        final double differenceYaw = Math.abs(deltaYaw - lastDeltaYaw);
        final double differencePitch = Math.abs(deltaPitch - lastDeltaPitch);

        final double joltYaw = Math.abs(differenceYaw - deltaYaw);
        final double joltPitch = Math.abs(differencePitch - deltaPitch);

        final boolean cinematic = (now - lastHighRate > 250L) || now - lastSmooth < 9000L;

        if (joltYaw > 1.0 && joltPitch > 1.0) {
            this.lastHighRate = now;
        }

        if (deltaPitch > 0.0 && deltaPitch > 0.0) {
            yawSamples.add(deltaYaw);
            pitchSamples.add(deltaPitch);
        }

        if (yawSamples.size() == 20 && pitchSamples.size() == 20) {
            // Get the cerberus/positive graph of the sample-lists
            final GraphUtil.GraphResult resultsYaw =    GraphUtil.getGraph(yawSamples);
            final GraphUtil.GraphResult resultsPitch = GraphUtil.getGraph(pitchSamples);

            // Negative values
            final int negativesYaw = resultsYaw.getNegatives();
            final int negativesPitch = resultsPitch.getNegatives();

            // Positive values
            final int positivesYaw = resultsYaw.getPositives();
            final int positivesPitch = resultsPitch.getPositives();

            // Cinematic camera usually does this on *most* speeds and is accurate for the most part.
            if (positivesYaw > negativesYaw || positivesPitch > negativesPitch) {
                this.lastSmooth = now;
            }

            yawSamples.clear();
            pitchSamples.clear();
        }

        playerData.setCinematic(cinematic);

        this.lastDeltaYaw = deltaYaw;
        this.lastDeltaPitch = deltaPitch;
    }
}
