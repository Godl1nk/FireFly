package rip.firefly.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BanEvent extends Event implements Cancellable {
    private static HandlerList handlers = new HandlerList();
    private Player player;
    private Integer vl;
    private String checkName;
    private boolean cancelled;

    public BanEvent(Player player, String checkName, Integer vl) {
        this.player = player;
        this.vl = vl;
        this.checkName = checkName;
    }

    public Integer getVl() {
        return vl;
    }

    public Player getPlayer() {
        return player;
    }

    public static void setHandlers(HandlerList handlers) {
        BanEvent.handlers = handlers;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setVl(Integer vl) {
        this.vl = vl;
    }

    public String getCheckName() {
        return checkName;
    }

    public void setCheckName(String checkName) {
        this.checkName = checkName;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}