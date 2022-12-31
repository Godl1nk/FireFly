package ac.firefly.check.impl.movement.scaffold;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import ac.firefly.data.PlayerData;
import ac.firefly.managers.PluginManager;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.List;

public class ScaffoldA extends Check {
    public ScaffoldA() {
        super("Scaffold (A)", CheckType.WORLD, true);
    }


    private final int SIZE = 20;

    @EventHandler
    public void onMove(PlayerMoveEvent event)
    {
        Player player = event.getPlayer();
        PlayerData playerData = PluginManager.instance.getDataManager().getData(player);

        List<Double> avgPitches = (List<Double>) playerData.getAverageScaffoldPitches();

        if (avgPitches == null)
        {
            avgPitches = new ArrayList<>();
        }

        avgPitches.add(0, (double) player.getLocation().getPitch());

        for (int i = SIZE; i < avgPitches.size(); i++)
        {
            avgPitches.remove(i);
        }

        playerData.setAverageScaffoldPitches(avgPitches);
 //
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = PluginManager.instance.getDataManager().getData(player);

        List<Double> avgPitches = (List<Double>) playerData.getAverageScaffoldPitches();

        if (avgPitches != null)
        {
            if (avgPitches.size() > SIZE)
            {
                if (!event.getBlock().getRelative(BlockFace.DOWN).getType().isSolid())
                {
                    double total = 0;

                    for (double d : avgPitches)
                    {
                        total += d;
                    }

                    total /= avgPitches.size();

                    double diff = Math.abs((player.getLocation().getPitch() - total));

                    if (diff < 30)
                    {
                        return;
                    }

                    flag(player, "A", "Pitches: " + avgPitches);
                }
            }
        }
    }

}
