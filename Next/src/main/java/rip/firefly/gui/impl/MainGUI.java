package rip.firefly.gui.impl;

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
import rip.firefly.FireFly;
import rip.firefly.bridge.data.BridgeData;
import rip.firefly.gui.GUI;
import rip.firefly.manager.ConfigManager;

import java.util.ArrayList;

/**
 * @author OmenDoesStuff
 * @see GUI
 */
public class MainGUI extends GUI implements Listener {
    /**
     * @since 5/8/22
     * @param player The Player To Show The GUI To
     */
    public static void openGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 27, ChatColor.YELLOW + "FireFly");

        ItemStack emptyItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
        ItemStack infoItem = new ItemStack(Material.PAPER);
        ItemStack restartItem = new ItemStack(Material.REDSTONE);
        ItemStack exitItem = new ItemStack(Material.DIAMOND_SWORD);

        ItemMeta infoMeta = infoItem.getItemMeta();

        infoMeta.setDisplayName(ChatColor.YELLOW + "Fire" + ChatColor.GOLD + "Fly");
        ArrayList<String> infoLore = new ArrayList<>();
        infoLore.add(ChatColor.GRAY + "Version: " + ChatColor.WHITE + FireFly.getVersion());
        infoLore.add(ChatColor.GRAY + "Type: " + ChatColor.WHITE + FireFly.getType().getName());
        infoLore.add(ChatColor.GRAY + "Total Bans: " + ChatColor.WHITE + BridgeData.totalBans);
        //infoLore.add(ChatColor.GRAY + "Total Flags: " + ChatColor.WHITE + BridgeData.totalFlags);
        //infoLore.add(ChatColor.GRAY + "Implementation: " + ChatColor.WHITE + Bukkit.getServer().getClass().getName().split("\\.")[3]);
        infoMeta.setLore(infoLore);
        infoItem.setItemMeta(infoMeta);
        gui.setItem(13, infoItem);

        ItemMeta restartMeta = restartItem.getItemMeta();

        restartMeta.setDisplayName(ChatColor.YELLOW + "Restart");
        ArrayList<String> rLore = new ArrayList<>();
        rLore.add(ChatColor.GRAY + "Restart FireFly");
        restartMeta.setLore(rLore);
        restartItem.setItemMeta(restartMeta);
        gui.setItem(16, restartItem);

        ItemMeta exitMeta = exitItem.getItemMeta();

        exitMeta.setDisplayName(ChatColor.YELLOW + "Menu");
        ArrayList<String> exitLore = new ArrayList<>();
        exitLore.add(ChatColor.GRAY + "Open The Actions Menu");
        exitMeta.setLore(exitLore);
        exitItem.setItemMeta(exitMeta);
        gui.setItem(10, exitItem);

        ItemMeta emeta = emptyItem.getItemMeta();
        emeta.setDisplayName(ChatColor.RESET + "");
        emptyItem.setItemMeta(emeta);
        for (int i = 0; i < 9; i++) {
            gui.setItem(i, emptyItem);
        }
        for (int i = 18; i < 27; i++) {
            gui.setItem(i, emptyItem);
        }

        gui.setItem(9, emptyItem);
        gui.setItem(17, emptyItem);

        gui.setItem(11, emptyItem);
        gui.setItem(12, emptyItem);
        gui.setItem(14, emptyItem);
        gui.setItem(15, emptyItem);



        player.openInventory(gui);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(e.getClickedInventory().getTitle().equalsIgnoreCase(ChatColor.YELLOW + "FireFly")){
            int clickedSlot = e.getSlot();
            HumanEntity humanEntity = e.getWhoClicked();
            if (humanEntity instanceof Player) {
                Player player = (Player) humanEntity;
                switch (clickedSlot) {
                    case 0:
                        player.sendMessage("This Server Uses FireFly");
                        player.sendMessage("Made With <3 By a lot of people");
                        e.setCancelled(true);
                        break;
                    case 10:
                        e.setCancelled(true);
                        player.closeInventory();
                        ActionsGUI.openGUI(player);
                        break;
                    case 16:
                        long before;
                        long after;
                        before = System.currentTimeMillis();
                        try {
                            ConfigManager.settings.reloadConfig();
                            ConfigManager.messages.reloadConfig();
                            after = System.currentTimeMillis();
                            long reloadTime = after - before;
                            // Switch for 2 statements? big brain
                            switch (FireFly.getType()) {
                                case ENTERPRISE:
                                    player.sendMessage(ChatColor.RED + "Reloaded FireFly Enterprise " + FireFly.getVersion() + " In " + reloadTime + "ms");
                                    break;
                                case STANDARD:
                                    player.sendMessage(ChatColor.RED + "Reloaded FireFly " + FireFly.getVersion() + " In " + reloadTime + "ms");
                            }
                        } catch (Exception ex) {
                            player.sendMessage(ChatColor.RED + "Failed To Reload!");
                            ex.printStackTrace();
                            e.setCancelled(true);
                        }
                        e.setCancelled(true);
                        break;
                }
                e.setCancelled(true);
            }
            e.setCancelled(true);
        }
    }
}
