package rip.firefly.gui.impl;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import rip.firefly.gui.GUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import rip.firefly.manager.ConfigManager;
import rip.firefly.manager.DataManager;
import rip.firefly.util.misc.ColorUtil;

import java.util.ArrayList;


public class ActionsGUI extends GUI implements Listener {
    public static void openGUI(Player p) {
        Inventory gui = Bukkit.createInventory(null, 27, ChatColor.YELLOW + "Menu");

        ItemStack emptyItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
        ItemStack checksItem = new ItemStack(Material.DIAMOND_SWORD);
        ItemStack restartItem = new ItemStack(Material.REDSTONE);
        ItemStack exitItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);

        ItemMeta eMeta = emptyItem.getItemMeta();
        eMeta.setDisplayName(ChatColor.RESET + "");

        ItemMeta emeta = emptyItem.getItemMeta();
        emeta.setDisplayName(ChatColor.RESET + "");
        emptyItem.setItemMeta(eMeta);

        for (int i = 0; i < 27; i++) {
            gui.setItem(i, emptyItem);
        }


        ItemMeta exMeta = exitItem.getItemMeta();
        exMeta.setDisplayName(ChatColor.RED + "Back");
        exitItem.setItemMeta(exMeta);
        //for (int i = 0; i < 9; i++) {
        //    gui.setItem(i, exitItem);
        //};

        gui.setItem(0, exitItem);
        gui.setItem(9, exitItem);
        gui.setItem(18, exitItem);

        ItemMeta chMeta = checksItem.getItemMeta();

        chMeta.setDisplayName(ChatColor.YELLOW + "Checks");
        ArrayList<String> exitLore = new ArrayList<>();
        exitLore.add(ChatColor.GRAY + "View Checks");
        chMeta.setLore(exitLore);
        checksItem.setItemMeta(chMeta);
        gui.setItem(11, checksItem);

        ItemMeta restartMeta = restartItem.getItemMeta();

        restartMeta.setDisplayName(ChatColor.YELLOW + "Toggle Alerts");
        ArrayList<String> rLore = new ArrayList<>();
        rLore.add(ChatColor.GRAY + "Toggle Your FireFly Alerts");
        restartMeta.setLore(rLore);
        restartItem.setItemMeta(restartMeta);
        gui.setItem(16, restartItem);


        p.openInventory(gui);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(e.getClickedInventory().getTitle().equalsIgnoreCase(ChatColor.YELLOW + "Menu")){
            int clickedSlot = e.getSlot();
            HumanEntity humanEntity = e.getWhoClicked();
            if (humanEntity instanceof Player) {
                Player player = (Player) humanEntity;
                switch (clickedSlot) {
                    case 0:
                        player.closeInventory();
                        MainGUI.openGUI(player);
                        e.setCancelled(true);
                        break;
                    case 9:
                        player.closeInventory();
                        MainGUI.openGUI(player);
                        e.setCancelled(true);
                        break;
                    case 18:
                        player.closeInventory();
                        MainGUI.openGUI(player);
                        e.setCancelled(true);
                        break;
                    case 11:
                        e.setCancelled(true);
                        player.closeInventory();
                        TypeGUI.openGUI(player);
                        break;
                    case 16:
                        FileConfiguration msg = ConfigManager.messages.getConfiguration();
                        if (!DataManager.getData(player).isAlerts()) {
                            player.sendMessage(ColorUtil.translate(ConfigManager.messages.getConfiguration().getString("enableAlerts")));
                        } else {
                            player.sendMessage(ColorUtil.translate(ConfigManager.messages.getConfiguration().getString("disableAlerts")));
                        }
                        DataManager.getData(player).setAlerts(!DataManager.getData(player).isAlerts());
                        e.setCancelled(true);
                        break;
                }
                e.setCancelled(true);
            }
            e.setCancelled(true);
        }
    }
}
