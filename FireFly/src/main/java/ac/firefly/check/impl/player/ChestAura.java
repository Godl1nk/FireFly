package ac.firefly.check.impl.player;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import ac.firefly.util.fovutils.BlockPathFinder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ChestAura extends Check {
    public ChestAura() {
        super("ChestAura (A)", CheckType.WORLD, true);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (e.getClickedBlock().getType() == Material.CHEST || e.getClickedBlock().getType() == Material.TRAPPED_CHEST || e.getClickedBlock().getType() == Material.ENDER_CHEST) {
                Player p = e.getPlayer();
                if ((e.getClickedBlock().getLocation().distance(p.getPlayer().getEyeLocation()) > 2)
                        && !BlockPathFinder.line(p.getPlayer().getEyeLocation(), e.getClickedBlock().getLocation()).contains(e.getClickedBlock()) && !e.isCancelled()) {
                    flag(p, "ChestAura/GhostHand", null);
                    e.setCancelled(true);
                }
            }
        }
    }
}
