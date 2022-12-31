package ac.firefly.events.packet;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PacketServerKeepAliveEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Player player;

    public PacketServerKeepAliveEvent(Player player) {
        this.player = player;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return this.player;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
