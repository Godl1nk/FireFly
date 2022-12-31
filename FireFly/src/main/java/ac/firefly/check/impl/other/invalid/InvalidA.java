package ac.firefly.check.impl.other.invalid;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;

public class InvalidA extends Check {
    public InvalidA() {
        super("Invalid (A)", CheckType.MISC, true);
    }

    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent event){
        if(event.getItemInHand().equals(Material.SKULL_ITEM) || event.getItemInHand().equals(Material.ARMOR_STAND) || event.getItemInHand().equals(Material.MONSTER_EGG)) {
            return;
        }
        if(event.getBlock().getType() != event.getItemInHand().getType()){
            flag(event.getPlayer(), "A", "Placed: " + event.getBlock().getType().name() + " Hand: " + event.getItemInHand().getType().name());
        }
    }
}
