package ac.firefly.handlers;

import ac.firefly.Firefly;
import ac.firefly.events.update.UpdateEvent;
import ac.firefly.events.update.UpdateType;
import org.bukkit.plugin.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;

public class UpdateHandler implements Runnable
{
    private Firefly firefly;
    private int updater;

    public UpdateHandler(final Firefly firefly) {
        super();
        this.firefly = firefly;
        this.updater = Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin)this.firefly, (Runnable)this, 0L, 1L);
    }

    public void Disable() {
        Bukkit.getScheduler().cancelTask(this.updater);
    }

    @Override
    public void run() {
        UpdateType[] values;
        for (int length = (values = UpdateType.values()).length, i = 0; i < length; ++i) {
            final UpdateType updateType = values[i];
            if (updateType != null) {
                if (updateType.Elapsed()) {
                    try {
                        final UpdateEvent event = new UpdateEvent(updateType);
                        //Bukkit.getPluginManager().callEvent(event);
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }
}