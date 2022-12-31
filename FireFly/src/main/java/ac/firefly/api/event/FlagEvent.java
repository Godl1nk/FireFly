package ac.firefly.api.event;

import ac.firefly.api.FireflyAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FlagEvent extends Event {
    private static HandlerList handlers = new HandlerList();
    private Player player;
    private String data;
    private String debugData;
    private Integer vl;
    private String checkName;

    public FlagEvent(Player player, String checkName, String data, String debugData, Integer vl) {
        this.player = player;
        this.data = data;
        this.debugData = debugData;
        this.vl = vl;
        this.checkName = checkName;
    }

    public Integer getVl() {
        return vl;
    }

    public Player getPlayer() {
        return player;
    }

    public String getData() {
        return data;
    }

    public String getDebugData() {
        return debugData;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setDebugData(String debugData) {
        this.debugData = debugData;
    }

    public static void setHandlers(HandlerList handlers) {
        FlagEvent.handlers = handlers;
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
        // TODO Auto-generated method stub

        return handlers;
    }
}
