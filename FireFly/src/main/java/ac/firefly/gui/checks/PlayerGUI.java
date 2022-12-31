package ac.firefly.gui.checks;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import ac.firefly.gui.MenuGUI;
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

public class PlayerGUI implements Listener {
    public static void openGUI(Player p) {
        Inventory gui = Bukkit.createInventory(null, 54, ChatColor.YELLOW + "Player");

        int page = 1;

        ItemStack emptyItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
        ItemStack exitItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);

        ItemMeta eMeta = emptyItem.getItemMeta();
        eMeta.setDisplayName(ChatColor.RESET + "");

        ItemMeta exMeta = exitItem.getItemMeta();
        exMeta.setDisplayName(ChatColor.RED + "Back");
        exitItem.setItemMeta(exMeta);
        for (int i = 0; i < 9; i++) {
            gui.setItem(i, exitItem);
        }

    //    ArrayList<String> checkNames = new ArrayList<>();

            int i = 9;
            for (Check c : PluginManager.instance.getDataManager().getChecks()) {

                if(c.getType() != CheckType.PLAYER) {
                    continue;
                }
                ItemStack is = new ItemStack(Material.DIAMOND_SWORD, 1);
                ItemMeta isMeta = is.getItemMeta();
                isMeta.setDisplayName(ChatColor.BLUE + c.getName());
                ArrayList<String> checkLore = new ArrayList<>();
                if(PluginManager.instance.getDataManager().getCheckByName(c.getName()).isEnabled()) {
                    checkLore.add(ChatColor.GRAY + "Enabled: " + ChatColor.GREEN + "True");
                } else {
                    checkLore.add(ChatColor.GRAY + "Enabled: " + ChatColor.RED + "False");
                }
                isMeta.setLore(checkLore);
                is.setItemMeta(isMeta);
                gui.setItem(i, is);
                i++;
        }


        p.openInventory(gui);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(e.getClickedInventory().getTitle().equalsIgnoreCase(ChatColor.YELLOW + "Player")){
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
                    case 1:
                        player.closeInventory();
                        MenuGUI.openGUI(player);
                        e.setCancelled(true);
                        break;
                    case 2:
                        player.closeInventory();
                        MenuGUI.openGUI(player);
                        e.setCancelled(true);
                        break;
                    case 3:
                        player.closeInventory();
                        MenuGUI.openGUI(player);
                        e.setCancelled(true);
                        break;
                    case 4:
                        player.closeInventory();
                        MenuGUI.openGUI(player);
                        e.setCancelled(true);
                        break;
                    case 5:
                        player.closeInventory();
                        MenuGUI.openGUI(player);
                        e.setCancelled(true);
                        break;
                    case 6:
                        player.closeInventory();
                        MenuGUI.openGUI(player);
                        e.setCancelled(true);
                        break;
                    case 7:
                        player.closeInventory();
                        MenuGUI.openGUI(player);
                        e.setCancelled(true);
                        break;
                    case 8:
                        player.closeInventory();
                        MenuGUI.openGUI(player);
                        e.setCancelled(true);
                        break;
                }
                e.setCancelled(true);
            }
            if(clickedSlot > 8) {
                PluginManager.instance.getDataManager().getCheckByName(ChatColor.stripColor(e.getClickedInventory().getItem(clickedSlot).getItemMeta().getDisplayName())).setEnabled(!(PluginManager.instance.getDataManager().getCheckByName(ChatColor.stripColor(e.getClickedInventory().getItem(clickedSlot).getItemMeta().getDisplayName())).isEnabled()));
                e.setCancelled(true);
                Player pxe = (Player) e.getWhoClicked();
                pxe.closeInventory();
                openGUI(pxe);
            }
            e.setCancelled(true);
        }
    }
}
