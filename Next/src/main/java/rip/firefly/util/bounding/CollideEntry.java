package rip.firefly.util.bounding;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.block.Block;

@AllArgsConstructor
@Getter
public class CollideEntry {
    private final Block block;
    private final RBoundingBox boundingBox;

    public boolean isChunkLoaded() {
        return isChunkLoaded(block.getLocation());
    }

    public static boolean isChunkLoaded(Location location) {
        return (location.getWorld().isChunkLoaded(location.getBlockX() >> 4, location.getBlockZ() >> 4));
    }
}