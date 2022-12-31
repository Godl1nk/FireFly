package ac.firefly.gui;

import ac.firefly.Firefly;
import ac.firefly.managers.ConfigManager;
import ac.firefly.util.formatting.Color;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class MainGUI implements Listener {

    public static void openGUI(Player p) {
        Inventory gui = Bukkit.createInventory(null, 27, ChatColor.YELLOW + "FireFly");

        ItemStack emptyItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
        ItemStack infoItem = new ItemStack(Material.PAPER);
        ItemStack restartItem = new ItemStack(Material.REDSTONE);
        ItemStack exitItem = new ItemStack(Material.DIAMOND_SWORD);

        ItemMeta infoMeta = infoItem.getItemMeta();

        infoMeta.setDisplayName(ChatColor.YELLOW + "Fire" + ChatColor.GOLD + "Fly");
        ArrayList<String> infoLore = new ArrayList<>();
        infoLore.add(ChatColor.GRAY + "Version: " + ChatColor.WHITE + Firefly.getVersion());
        infoLore.add(ChatColor.GRAY + "Total Bans: " + ChatColor.WHITE + Firefly.banCount);
        infoLore.add(ChatColor.GRAY + "Implementation: " + ChatColor.WHITE + Bukkit.getServer().getClass().getName().split("\\.")[3]);
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



        p.openInventory(gui);
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
                        player.sendMessage("Made With <3 By OmenDoesStuff & ThePython");
                        e.setCancelled(true);
                        break;
                    case 10:
                        e.setCancelled(true);
                        player.closeInventory();
                        MenuGUI.openGUI(player);
                        break;
                    case 16:
                        FileConfiguration config = ConfigManager.settings.getConfiguration();
                        FileConfiguration msg = ConfigManager.messages.getConfiguration();
                        player.sendMessage(Color.translate(msg.getString("commands.firefly.reloading")));
                        long before;
                        long after;
                        before = System.currentTimeMillis();
                        try {
                            ConfigManager.settings.reloadConfig();
                            ConfigManager.messages.reloadConfig();
                            after = System.currentTimeMillis();
                            long reloadTime = after - before;
                            player.sendMessage(Color.translate(msg.getString("commands.firefly.reload_success").replace("$time", "" + reloadTime)));
                        } catch (Exception ex) {
                            player.sendMessage(Color.translate(msg.getString("commands.firefly.reload_failed")));
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
