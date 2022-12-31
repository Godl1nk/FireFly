package ac.firefly.check.impl.movement.scaffold;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.ArrayList;

public class ScaffoldC extends Check {

    static ArrayList<Material> blockItems = new ArrayList<>();

    public ScaffoldC() {
        super("Scaffold (C)", CheckType.WORLD, true);
    }

    static {
        blockItems.add(Material.BANNER);
        blockItems.add(Material.ARMOR_STAND);
        blockItems.add(Material.SKULL_ITEM);
        blockItems.add(Material.SKULL);
        blockItems.add(Material.MONSTER_EGG);
        blockItems.add(Material.MONSTER_EGGS);
        blockItems.add(Material.SIGN);
        blockItems.add(Material.SIGN_POST);
        blockItems.add(Material.WALL_SIGN);
        blockItems.add(Material.AIR);
        blockItems.add(Material.ANVIL);
        blockItems.add(Material.STRING);
        blockItems.add(Material.CARPET);
        blockItems.add(Material.ITEM_FRAME);
        blockItems.add(Material.GRASS);
        blockItems.add(Material.DOUBLE_PLANT);
        blockItems.add(Material.LONG_GRASS);
        blockItems.add(Material.FLINT_AND_STEEL);
        blockItems.add(Material.FIREWORK);
        blockItems.add(Material.PAINTING);
        blockItems.add(Material.POTION);
        blockItems.add(Material.SEEDS);
        blockItems.add(Material.MELON_SEEDS);
        blockItems.add(Material.PUMPKIN_SEEDS);
        blockItems.add(Material.RECORD_3);
        blockItems.add(Material.RECORD_4);
        blockItems.add(Material.RECORD_5);
        blockItems.add(Material.RECORD_6);
        blockItems.add(Material.RECORD_7);
        blockItems.add(Material.RECORD_8);
        blockItems.add(Material.RECORD_9);
        blockItems.add(Material.RECORD_10);
        blockItems.add(Material.RECORD_11);
        blockItems.add(Material.RECORD_12);
        blockItems.add(Material.GREEN_RECORD);
        blockItems.add(Material.GOLD_RECORD);
        blockItems.add(Material.YELLOW_FLOWER);
        blockItems.add(Material.RED_ROSE);
        blockItems.add(Material.SNOW);
        blockItems.add(Material.SNOW_BALL);
        blockItems.add(Material.BOW);
        blockItems.add(Material.VINE);
        blockItems.add(Material.WRITTEN_BOOK);
        blockItems.add(Material.BOOK_AND_QUILL);
        blockItems.add(Material.BOOK);
        blockItems.add(Material.REDSTONE_WIRE);
        blockItems.add(Material.REDSTONE);
        blockItems.add(Material.STEP);
        blockItems.add(Material.WOOD_DOUBLE_STEP);
    }


    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent event){
        if(blockItems.contains(event.getItemInHand().getType())) {
            return;
        }
        if(event.getBlock().getType() != event.getItemInHand().getType()){
            flag(event.getPlayer(), "C", "Placed: " + event.getBlock().getType().name() + " Hand: " + event.getItemInHand().getType().name());
        }
    }
}
