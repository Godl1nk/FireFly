package rip.firefly.gui;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public abstract class GUI implements Listener {
    @EventHandler
    public abstract void onClick(InventoryClickEvent e);
}
