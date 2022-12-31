package ac.firefly.check.impl.movement.scaffold;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import ac.firefly.util.math.MathUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;

public class ScaffoldB extends Check {
    public ScaffoldB() {
        super("Scaffold (B)", CheckType.WORLD, true);
    }

    // Expand Check //

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        final double xOffset = MathUtils.offset(event.getPlayer().getLocation().getX(), event.getBlockAgainst().getX());
        final double zOffset = MathUtils.offset(event.getPlayer().getLocation().getZ(), event.getBlockAgainst().getZ());

        boolean flag = false;

        switch (event.getBlock().getFace(event.getBlockAgainst()))
        {
            case EAST:
                flag = xOffset <= 0;
                break;
            case WEST:
                flag = xOffset <= 1;
                break;
            case NORTH:
                flag = zOffset <= 1;
                break;
            case SOUTH:
                flag = zOffset <= 0;
                break;
            default:
                flag = false;
                break;
        }

        if (flag)
        {
            flag(event.getPlayer(), "B", "Pos: X: " + xOffset + " Z: " + zOffset);
        }
    }
}
