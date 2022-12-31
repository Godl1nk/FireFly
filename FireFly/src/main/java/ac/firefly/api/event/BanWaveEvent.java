package ac.firefly.api.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BanWaveEvent extends Event implements Cancellable {

    private boolean cancelled = false;
    private static HandlerList handlers = new HandlerList();
    private static boolean started = false;
    private static boolean waveHappeningNow = false;

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean b) {
        this.started = b;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
