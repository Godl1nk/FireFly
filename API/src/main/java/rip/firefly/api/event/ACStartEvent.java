package rip.firefly.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ACStartEvent extends Event {
    private static HandlerList handlers = new HandlerList();
    private String version;
    private String hwid;
    private String packageType;

    public ACStartEvent(String version, String hwid, String packageType) {
        this.packageType = packageType;
        this.version = version;
        this.hwid = hwid;
    }

    public String getPackageType() {
        return packageType;
    }

    public String getHwid() {
        return hwid;
    }

    public String getVersion() {
        return version;
    }

    public static void setHandlers(HandlerList handlers) {
        ACStartEvent.handlers = handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}