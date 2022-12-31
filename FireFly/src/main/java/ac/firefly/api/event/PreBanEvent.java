package ac.firefly.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PreBanEvent extends Event {
    private static HandlerList handlers = new HandlerList();
    private Player player;


    public PreBanEvent(Player player) {
        this.player = player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
