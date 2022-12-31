package ac.firefly.gui;

import ac.firefly.check.Check;
import ac.firefly.gui.checks.*;
import ac.firefly.managers.PluginManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class CheckGUI implements Listener {
    public static void openGUI(Player p) {
        Inventory gui = Bukkit.createInventory(null, 27, ChatColor.YELLOW + "Checks");


        ItemStack emptyItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
        ItemStack exitItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);

        ItemStack cItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 4);
        ItemMeta cMeta = emptyItem.getItemMeta();
        cMeta.setDisplayName(ChatColor.YELLOW + "Combat");
        cItem.setItemMeta(cMeta);

        ItemStack mItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 4);
        ItemMeta mMeta = emptyItem.getItemMeta();
        mMeta.setDisplayName(ChatColor.YELLOW + "Misc");
        mItem.setItemMeta(mMeta);

        ItemStack mvItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 4);
        ItemMeta mvMeta = emptyItem.getItemMeta();
        mvMeta.setDisplayName(ChatColor.YELLOW + "Movement");
        mvItem.setItemMeta(mvMeta);

        ItemStack pItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 4);
        ItemMeta pMeta = emptyItem.getItemMeta();
        pMeta.setDisplayName(ChatColor.YELLOW + "Player");
        pItem.setItemMeta(pMeta);

        ItemStack wItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 4);
        ItemMeta wMeta = emptyItem.getItemMeta();
        wMeta.setDisplayName(ChatColor.YELLOW + "World");
        wItem.setItemMeta(wMeta);

        ItemMeta eMeta = emptyItem.getItemMeta();
        eMeta.setDisplayName(ChatColor.RESET + "");

        ItemMeta exMeta = exitItem.getItemMeta();
        exMeta.setDisplayName(ChatColor.RED + "Back");


        exitItem.setItemMeta(exMeta);
        for (int i = 0; i < 27; i++) {
            gui.setItem(i, emptyItem);
        }

        gui.setItem(0, exitItem);
        gui.setItem(9, exitItem);
        gui.setItem(18, exitItem);

        gui.setItem(11, cItem);
        gui.setItem(12, mvItem);
        gui.setItem(13, pItem);
        gui.setItem(14, mItem);
        gui.setItem(15, wItem);

        p.openInventory(gui);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(e.getClickedInventory().getTitle().equalsIgnoreCase(ChatColor.YELLOW + "Checks")){
            int clickedSlot = e.getSlot();
            HumanEntity humanEntity = e.getWhoClicked();
            if (humanEntity instanceof Player) {
                Player player = (Player) humanEntity;
                switch (clickedSlot) {
                    case 0:
                        player.closeInventory();
                        MenuGUI.openGUI(player);
                        e.setCancelled(true);
                        break;
                    case 9:
                        player.closeInventory();
                        MenuGUI.openGUI(player);
                        e.setCancelled(true);
                        break;
                    case 18:
                        player.closeInventory();
                        MenuGUI.openGUI(player);
                        e.setCancelled(true);
                        break;
                    case 11:
                        player.closeInventory();
                        CombatGUI.openGUI(player);
                        e.setCancelled(true);
                        break;
                    case 12:
                        player.closeInventory();
                        MovementGUI.openGUI(player);
                        e.setCancelled(true);
                        break;
                    case 13:
                        player.closeInventory();
                        PlayerGUI.openGUI(player);
                        e.setCancelled(true);
                        break;
                    case 14:
                        player.closeInventory();
                        MiscellaneousGUI.openGUI(player);
                        e.setCancelled(true);
                        break;
                    case 15:
                        player.closeInventory();
                        WorldGUI.openGUI(player);
                        e.setCancelled(true);
                        break;
                }
                e.setCancelled(true);
            }
        }
    }
}
