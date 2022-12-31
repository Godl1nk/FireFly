package ac.firefly.tasks;

import ac.firefly.Firefly;
import ac.firefly.api.event.BanWaveEvent;
import ac.firefly.managers.PunishManager;
import ac.firefly.managers.ConfigManager;
import ac.firefly.util.formatting.Color;
import ac.firefly.util.interaction.Crash;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class WaveTask extends BukkitRunnable {

    @Override
    public void run() {
        final BanWaveEvent event = new BanWaveEvent();
        Bukkit.getPluginManager().callEvent(event);
        for(Player p : PunishManager.banWave.keySet()) {
            FileConfiguration msg = ConfigManager.messages.getConfiguration();
            if(p.isOnline()) {
                if(ConfigManager.settings.getConfiguration().getBoolean("ban.crash")) {
                    Crash.crashPlayer(p);
                }
            }
            Firefly.banPlayer(p);
            Bukkit.broadcastMessage(Color.translate(msg.getString("punish.bannedInWave")
                    .replace("$prefix", msg.getString("prefix"))
                    .replace("$player", p.getName())));
            PunishManager.removePunish(p);
        }
        FileConfiguration msg = ConfigManager.messages.getConfiguration();
        Bukkit.broadcastMessage(msg.getString(Color.translate("punish.waveEnded")
                .replace("$prefix", msg.getString("prefix"))));
    }
}
