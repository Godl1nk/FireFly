package rip.firefly.api.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ACLoadEvent extends Event {
    private static HandlerList handlers = new HandlerList();

    public static void setHandlers(HandlerList handlers) {
        ACLoadEvent.handlers = handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}