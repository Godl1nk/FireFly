package rip.firefly.gui.impl;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import rip.firefly.check.AbstractCheck;
import rip.firefly.check.CheckType;
import rip.firefly.gui.GUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import rip.firefly.manager.DataManager;
import rip.firefly.util.misc.Pair;

import java.util.ArrayList;
import java.util.HashMap;


public class ChecksGUI extends GUI implements Listener {
    static int gpage;
    static int maxpage;
    static CheckType gtype;
    static HashMap<Pair<Integer, Integer>, AbstractCheck> mappedChecks = new HashMap<>();
    static HashMap<Pair<Integer, Integer>, ItemStack> mappedItems = new HashMap<>();
    public static void openGUI(Player p, CheckType type, int page) {

        gpage = page;
        gtype = type;
        mappedChecks.clear();
        mappedItems.clear();

        Inventory gui = Bukkit.createInventory(null, 27, ChatColor.YELLOW + "Checks");

        ItemStack emptyItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
        ItemStack exitItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);

        ItemMeta eMeta = emptyItem.getItemMeta();
        eMeta.setDisplayName(ChatColor.RESET + "");

        ItemMeta emeta = emptyItem.getItemMeta();
        emeta.setDisplayName(ChatColor.RESET + "");
        emptyItem.setItemMeta(eMeta);


        ItemMeta exMeta = exitItem.getItemMeta();
        exMeta.setDisplayName(ChatColor.RED + "Back");
        exitItem.setItemMeta(exMeta);
        for (int i = 0; i < 9; i++) {
            gui.setItem(i, exitItem);
        }

        if(gpage < maxpage) {
            ItemStack backItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
            ItemMeta backMeta = backItem.getItemMeta();
            backMeta.setDisplayName("Next Page");
            backItem.setItemMeta(backMeta);
            gui.setItem(8, backItem);
        }

        if(gpage > 0) {
            ItemStack backItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
            ItemMeta backMeta = backItem.getItemMeta();
            backMeta.setDisplayName("Last Page");
            backItem.setItemMeta(backMeta);
            gui.setItem(0, backItem);
        }

        int in = 9;
        int cpage = 0;
        for(AbstractCheck c : DataManager.getData(p).getChecks().values()) {
            if (in >= 27) cpage++;
            if (c.getType() == type) {
                mappedChecks.put(new Pair<>(cpage, in), c);
                ItemStack checkItem = new ItemStack(Material.GOLD_SWORD);
                ItemMeta checkMeta = checkItem.getItemMeta();
                checkMeta.setDisplayName((c.isEnabled() ? ChatColor.GREEN : ChatColor.RED) + c.getCheckName() + " (" + c.getSubType() + ")");
                ArrayList<String> checkLore = new ArrayList<>();
                checkLore.add(ChatColor.GRAY + "Enabled: " + (c.isEnabled() ? ChatColor.GREEN + "true" : ChatColor.RED + "false"));
                checkLore.add(ChatColor.GRAY + "Autobans: " + (c.isAutobans() ? ChatColor.GREEN + "true" : ChatColor.RED + "false"));
                if (c.isEnterprise()) {
                    checkLore.add(ChatColor.GRAY + "Enterprise: " + ChatColor.GREEN + "true");
                }
                checkLore.add(ChatColor.GRAY + "Threshold: " + ChatColor.WHITE + c.getThreshold());
                checkMeta.setLore(checkLore);
                checkItem.setItemMeta(checkMeta);

                mappedItems.put(new Pair<>(cpage, in), checkItem);
                in++;
            }
            maxpage = cpage;
        }

        for(int i = 9; i < 27; i++) {
            gui.setItem(i, mappedItems.get(new Pair<>(page, i)));
        }

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
                    case 0: {
                        if (gpage > 0) {
                            openGUI(player, gtype, gpage - 1);
                        }
                        e.setCancelled(true);
                        return;
                    }

                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                        player.closeInventory();
                        TypeGUI.openGUI(player);
                        e.setCancelled(true);
                        return;

                    case 8:
                        if(gpage < maxpage) {
                            player.closeInventory();
                            openGUI(player, gtype, gpage + 1);
                            e.setCancelled(true);
                        }
                        return;
                }
                if(e.getClick() == ClickType.LEFT) {
                    mappedChecks.get(new Pair<>(gpage, clickedSlot)).setEnabled(!mappedChecks.get(new Pair<>(gpage, clickedSlot)).isEnabled());
                    player.closeInventory();
                    openGUI(player, gtype, gpage);
                } else if (e.getClick() == ClickType.RIGHT) {
                    mappedChecks.get(new Pair<>(gpage, clickedSlot)).setAutobans(!mappedChecks.get(new Pair<>(gpage, clickedSlot)).isAutobans());
                    player.closeInventory();
                    openGUI(player, gtype, gpage);
                }
                e.setCancelled(true);
            }
            e.setCancelled(true);
        }
    }
}
