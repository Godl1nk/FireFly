package ac.firefly.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PostBanEvent extends Event {
    private static HandlerList handlers = new HandlerList();
    private Player player;
    private String check;


    public PostBanEvent(Player player, String check) {
        this.player = player;
        this.check = check;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
