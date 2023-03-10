package rip.firefly.util.location;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 *
 * Credits to dawson/funkemunky for this
 *
 */
public class RPastLocation {
    private List<PlayerLocation> previousLocations;

    public RPastLocation() {
        previousLocations = new CopyOnWriteArrayList<>();
    }
    public PlayerLocation getPreviousLocation(long time) {
        return previousLocations.stream().min(Comparator.comparingLong(loc -> Math.abs(loc.getTimestamp() - (System.currentTimeMillis() - time)))).orElse(previousLocations.get(previousLocations.size() - 1));
    }

    public List<PlayerLocation> getEstimatedLocation(long time, long delta) {
        List<PlayerLocation> locs = new ArrayList<>();

        previousLocations.stream()
                .sorted(Comparator.comparingLong(loc -> Math.abs(loc.getTimestamp() - (System.currentTimeMillis() - time))))
                .filter(loc -> Math.abs(loc.getTimestamp() - (System.currentTimeMillis() - time)) < delta)
                .forEach(locs::add);
        return locs;
    }

    public List<PlayerLocation> getEstimatedLocation(long time, long ping, long delta) {
        return this.previousLocations
                .stream()
                .filter(loc -> time - loc.getTimestamp() > 0 && time - loc.getTimestamp() < ping + delta)
                .collect(Collectors.toList());
    }

    public void addLocation(Location location) {
        if (previousLocations.size() >= 8) {
            previousLocations.remove(0);
        }

        previousLocations.add(new PlayerLocation(location));
    }

    public void addLocation(PlayerLocation location) {
        if (previousLocations.size() >= 8) {
            previousLocations.remove(0);
        }

        previousLocations.add(location);
    }

    public List<PlayerLocation> getPreviousLocations() {
        return previousLocations;
    }
}