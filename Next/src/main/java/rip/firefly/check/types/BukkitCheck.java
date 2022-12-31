package rip.firefly.check.types;

import org.bukkit.event.Event;
import rip.firefly.check.AbstractCheck;
import rip.firefly.data.PlayerData;

@Deprecated
public abstract class BukkitCheck extends AbstractCheck {

    public BukkitCheck(PlayerData data) {
        super(data);
    }

    /**
     * The method to handle checks via Bukkit events
     * @param playerData The player to check
     * @param event The {@link Event} to pass to the check
     */
    public abstract void handle(PlayerData playerData, Event event);
}