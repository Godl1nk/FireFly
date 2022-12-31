package ac.firefly.events.packet;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PacketDigEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private Location block;
    public PacketDigEvent(Player player, Location block) {
        this.player = player;
        this.block = block;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Location getBlock() {
        return block;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
